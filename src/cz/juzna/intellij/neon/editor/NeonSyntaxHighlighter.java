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

	public static final TextAttributesKey UNKNOWN = TextAttributesKey.createTextAttributesKey("Bad character", HighlighterColors.BAD_CHARACTER);
	public static final TextAttributesKey COMMENT = TextAttributesKey.createTextAttributesKey("Comment", DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey IDENTIFIER = TextAttributesKey.createTextAttributesKey("Identifier", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey INTERPUNCTION = TextAttributesKey.createTextAttributesKey("Interpunction", DefaultLanguageHighlighterColors.DOT);
	public static final TextAttributesKey NUMBER = TextAttributesKey.createTextAttributesKey("Number", DefaultLanguageHighlighterColors.NUMBER);
	public static final TextAttributesKey CLASS_NAME = TextAttributesKey.createTextAttributesKey("ClassName", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
	public static final TextAttributesKey PHP_IDENTIFIER = TextAttributesKey.createTextAttributesKey("PhpIdentifier", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
	public static final TextAttributesKey PHP_STATIC_IDENTIFIER = TextAttributesKey.createTextAttributesKey("PhpStaticIdentifier", DefaultLanguageHighlighterColors.STATIC_FIELD);
	public static final TextAttributesKey METHOD = TextAttributesKey.createTextAttributesKey("Method", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
	public static final TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey("String", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey DATE = TextAttributesKey.createTextAttributesKey("Date", DefaultLanguageHighlighterColors.DOC_COMMENT);
	public static final TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey("Keyword", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey KEY_USAGE = TextAttributesKey.createTextAttributesKey("KeyUsage", DefaultLanguageHighlighterColors.METADATA);
	public static final TextAttributesKey PARAMETER_USAGE = TextAttributesKey.createTextAttributesKey("ParameterUsage", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG_VALUE);

	// Groups of IElementType's
	public static final TokenSet sBAD = TokenSet.create(NEON_UNKNOWN);
	public static final TokenSet sCOMMENTS = TokenSet.create(NEON_COMMENT);
	public static final TokenSet sIDENTIFIERS = TokenSet.create(NEON_KEY); //, NEON_IDENTIFIER, NEON_LITERAL);
	public static final TokenSet sINTERPUNCTION = TokenSet.create(NEON_LPAREN, NEON_RPAREN, NEON_LBRACE_CURLY, NEON_RBRACE_CURLY, NEON_LBRACE_SQUARE, NEON_RBRACE_SQUARE, NEON_ITEM_DELIMITER, NEON_ASSIGNMENT);
	public static final TokenSet sNUMBERS = TokenSet.create(NEON_NUMBER);
	public static final TokenSet sClassName = TokenSet.create(NEON_CLASS_REFERENCE, NEON_NAMESPACE_RESOLUTION, NEON_NAMESPACE_REFERENCE);
	public static final TokenSet sMethod = TokenSet.create(NEON_METHOD, NEON_PHP_STATIC_METHOD);
	public static final TokenSet sIdentifier = TokenSet.create(NEON_KEY_IDENTIFIER);
	public static final TokenSet sStaticIdentifier = TokenSet.create(NEON_PHP_STATIC_IDENTIFIER);
	public static final TokenSet sKEYWORDS = TokenSet.create(NEON_KEYWORD);
	public static final TokenSet sKeyUsages = TokenSet.create(NEON_KEY_USAGE);
	public static final TokenSet sParameterUsages = TokenSet.create(NEON_PARAMETER_USAGE, NEON_PARAMETER_LEFT, NEON_PARAMETER_RIGHT, NEON_PARAMETER_DELIMITER);
	public static final TokenSet sSTRING = TokenSet.create(NEON_STRING, NEON_SINGLE_QUOTE_LEFT, NEON_SINGLE_QUOTE_RIGHT, NEON_DOUBLE_QUOTE_LEFT, NEON_DOUBLE_QUOTE_RIGHT);
	public static final TokenSet sDATE = TokenSet.create(NEON_DATE_TIME);

	// Static container
	private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();

	// Fill in the map
	static {
		fillMap(ATTRIBUTES, sBAD,           UNKNOWN);
		fillMap(ATTRIBUTES, sCOMMENTS,      COMMENT);
		fillMap(ATTRIBUTES, sIDENTIFIERS,   IDENTIFIER);
		fillMap(ATTRIBUTES, sINTERPUNCTION, INTERPUNCTION);
		fillMap(ATTRIBUTES, sNUMBERS,       NUMBER);
		fillMap(ATTRIBUTES, sClassName,       CLASS_NAME);
		fillMap(ATTRIBUTES, sMethod,       METHOD);
		fillMap(ATTRIBUTES, sIdentifier,       PHP_IDENTIFIER);
		fillMap(ATTRIBUTES, sStaticIdentifier,       PHP_STATIC_IDENTIFIER);
		fillMap(ATTRIBUTES, sKEYWORDS,      KEYWORD);
		fillMap(ATTRIBUTES, sKeyUsages,      KEY_USAGE);
		fillMap(ATTRIBUTES, sParameterUsages,      PARAMETER_USAGE);
		fillMap(ATTRIBUTES, sSTRING,      STRING);
		fillMap(ATTRIBUTES, sDATE,      DATE);
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
