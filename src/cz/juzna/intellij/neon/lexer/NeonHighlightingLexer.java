package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.LookAheadLexer;
import com.intellij.psi.tree.IElementType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Lexer used for syntax highlighting
 *
 * It reuses the simple lexer, changing types of some tokens
 */
public class NeonHighlightingLexer extends LookAheadLexer {

	private IElementType lastToken = null;
	private boolean inKeyUsage = false;

	private static final Set<String> KEYWORDS = new HashSet<String>(Arrays.asList(
		new String[]{
			"true", "True", "TRUE", "yes", "Yes", "YES", "on", "On", "ON",
			"false", "False", "FALSE", "no", "No", "NO", "off", "Off", "OFF",
			"null", "Null", "NULL"
		}
	));

	public NeonHighlightingLexer(Lexer baseLexer) {
		super(baseLexer, 1);
	}

	@Override
	protected void addToken(int endOffset, IElementType type) {
		if (lastToken == NeonTokenTypes.NEON_NAMESPACE_RESOLUTION && (type == NeonTokenTypes.NEON_IDENTIFIER || type == NeonTokenTypes.NEON_METHOD)) {
			type = NeonTokenTypes.NEON_CLASS_REFERENCE;
		}

		boolean classUsagesItems = NeonTokenTypes.HIGHLIGHT_KEYWORD_ELEMENTS.contains(type);
		if ((lastToken == NeonTokenTypes.NEON_KEY_USAGE || inKeyUsage) && classUsagesItems) {
			type = NeonTokenTypes.NEON_KEY_USAGE;
			inKeyUsage = true;

		} else if (!classUsagesItems) {
			inKeyUsage = false;
		}

		super.addToken(endOffset, type);
		lastToken = type;
	}

	@Override
	protected void lookAhead(Lexer baseLexer) {
		IElementType currentToken = baseLexer.getTokenType();

		if (NeonTokenTypes.KEY_LITERALS.contains(currentToken) && KEYWORDS.contains(baseLexer.getTokenText())) {
			advanceLexer(baseLexer);
			replaceCachedType(0, NeonTokenTypes.NEON_KEYWORD);

		} else if (NeonTokenTypes.HIGHLIGHT_KEYWORD_ELEMENTS.contains(currentToken)) {
			boolean isNextColon = false;
			int counter = 1;
			while (NeonTokenTypes.HIGHLIGHT_KEYWORD_ELEMENTS.contains(currentToken) || currentToken == NeonTokenTypes.NEON_COLON || NeonTokenTypes.WHITESPACES.contains(currentToken)) {
				advanceLexer(baseLexer);
				currentToken = baseLexer.getTokenType();

				if (NeonTokenTypes.WHITESPACES.contains(currentToken)) {
					continue;
				}

				if (currentToken == NeonTokenTypes.NEON_COLON) {
					isNextColon = true;
					break;
				} else {
					counter++;
				}
			}

			if (isNextColon) {
				for (int i = 0; i < counter ; i++) {
					replaceCachedType(i, NeonTokenTypes.NEON_KEYWORD);
				}
			}

		} else if (NeonTokenTypes.KEY_LITERALS.contains(currentToken) || currentToken == NeonTokenTypes.NEON_STRING) {
			advanceLexer(baseLexer);

			if (baseLexer.getTokenType() == NeonTokenTypes.NEON_WHITESPACE) {
				advanceLexer(baseLexer);
			}

			if (baseLexer.getTokenType() == NeonTokenTypes.NEON_COLON) {
				advanceLexer(baseLexer);
				replaceCachedType(0, NeonTokenTypes.NEON_KEY);
			}

		} else {
			super.lookAhead(baseLexer);
		}
	}
}
