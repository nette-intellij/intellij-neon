package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.LookAheadLexer;
import com.intellij.psi.tree.IElementType;

/**
 * Lexer used for syntax highlighting
 *
 * It reuses the simple lexer, changing types of some tokens
 */
public class NeonHighlightingLexer extends LookAheadLexer {

	public NeonHighlightingLexer(Lexer baseLexer) {
		super(baseLexer, 1);
	}

	@Override
	protected void lookAhead(Lexer baseLexer) {
		IElementType currentToken = baseLexer.getTokenType();
		if (currentToken == NeonTokenTypes.NEON_LITERAL || currentToken == NeonTokenTypes.NEON_STRING) {
			advanceLexer(baseLexer);

			if (baseLexer.getTokenType() == NeonTokenTypes.NEON_WHITESPACE) {
				advanceLexer(baseLexer);
			}

			if (baseLexer.getTokenType() == NeonTokenTypes.NEON_SYMBOL && baseLexer.getTokenText().equals(":")) {
				advanceLexer(baseLexer);
				replaceCachedType(0, NeonTokenTypes.NEON_KEY);
			}

		} else {
			super.lookAhead(baseLexer);
		}
	}
}
