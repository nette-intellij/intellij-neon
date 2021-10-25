package cz.juzna.intellij.neon.editor;


import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.lexer.NeonHighlightingLexer;
import cz.juzna.intellij.neon.lexer.NeonLexer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static cz.juzna.intellij.neon.lexer.NeonTokenTypes.*;

public class NeonSyntaxHighlighter extends SyntaxHighlighterBase {

	public static final String            UNKNOWN_ID       = "Bad character";
	public static final TextAttributesKey UNKNOWN          = TextAttributesKey.createTextAttributesKey(UNKNOWN_ID, HighlighterColors.BAD_CHARACTER);

	public static final String            COMMENT_ID       = "Comment";
	public static final TextAttributesKey COMMENT          = TextAttributesKey.createTextAttributesKey(COMMENT_ID, DefaultLanguageHighlighterColors.LINE_COMMENT);

	public static final String            IDENTIFIER_ID    = "Identifier";
	public static final TextAttributesKey IDENTIFIER       = TextAttributesKey.createTextAttributesKey(IDENTIFIER_ID, DefaultLanguageHighlighterColors.KEYWORD);

	public static final String            INTERPUNCTION_ID = "Interpunction";
	public static final TextAttributesKey INTERPUNCTION    = TextAttributesKey.createTextAttributesKey(INTERPUNCTION_ID, DefaultLanguageHighlighterColors.DOT);

	public static final String            NUMBER_ID        = "Number";
	public static final TextAttributesKey NUMBER           = TextAttributesKey.createTextAttributesKey(NUMBER_ID, DefaultLanguageHighlighterColors.NUMBER);

	public static final String            KEYWORD_ID       = "Keyword";
	public static final TextAttributesKey KEYWORD          = TextAttributesKey.createTextAttributesKey(KEYWORD_ID, DefaultLanguageHighlighterColors.KEYWORD);

	// Groups of IElementType's
	public static final TokenSet sBAD           = TokenSet.create(NEON_UNKNOWN);
	public static final TokenSet sCOMMENTS      = TokenSet.create(NEON_COMMENT);
	public static final TokenSet sIDENTIFIERS   = TokenSet.create(NEON_KEY); //, NEON_IDENTIFIER, NEON_LITERAL);
	public static final TokenSet sINTERPUNCTION = TokenSet.create(NEON_BLOCK_INHERITENCE, NEON_LPAREN, NEON_RPAREN, NEON_LBRACE_CURLY, NEON_RBRACE_CURLY, NEON_LBRACE_SQUARE, NEON_RBRACE_SQUARE, NEON_ITEM_DELIMITER, NEON_ASSIGNMENT);
	public static final TokenSet sNUMBERS       = TokenSet.create(NEON_NUMBER);
	public static final TokenSet sKEYWORDS      = TokenSet.create(NEON_KEYWORD);


	// Static container
	private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<>();


	// Fill in the map
	static {
		fillMap(ATTRIBUTES, sBAD,           UNKNOWN);
		fillMap(ATTRIBUTES, sCOMMENTS,      COMMENT);
		fillMap(ATTRIBUTES, sIDENTIFIERS,   IDENTIFIER);
		fillMap(ATTRIBUTES, sINTERPUNCTION, INTERPUNCTION);
		fillMap(ATTRIBUTES, sNUMBERS,       NUMBER);
		fillMap(ATTRIBUTES, sKEYWORDS,      KEYWORD);
	}


	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new NeonHighlightingLexer(new NeonLexer());
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType type) {
		return pack(ATTRIBUTES.get(type));
	}
}
