package cz.juzna.intellij.neon.lexer;

import com.google.common.collect.ImmutableMap;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import java.util.Map;

/**
 * Types of tokens returned form lexer
 *
 * @author Jan Dolecek - juzna.cz@gmail.com
 */
public interface NeonTokenTypes
{
	IElementType NEON_STRING = new NeonTokenType("string");
	IElementType NEON_STRINGD = new NeonTokenType("stringd");
	IElementType NEON_SINGLE_QUOTE_LEFT = new NeonTokenType("single quote left");
	IElementType NEON_SINGLE_QUOTE_RIGHT = new NeonTokenType("single quote right");
	IElementType NEON_DOUBLE_QUOTE_LEFT = new NeonTokenType("double quote left");
	IElementType NEON_DOUBLE_QUOTE_RIGHT = new NeonTokenType("double quote right");

	IElementType NEON_CLASS_NAME = new NeonTokenType("className");
	IElementType NEON_METHOD = new NeonTokenType("method");
	IElementType NEON_KEY_IDENTIFIER = new NeonTokenType("phpIdentifier");
	IElementType NEON_PHP_STATIC_IDENTIFIER = new NeonTokenType("phpStaticIdentifier");
	IElementType NEON_SYMBOL = new NeonTokenType("symbol"); // use a symbol or brace instead (see below)
	IElementType NEON_COMMENT = new NeonTokenType("comment");
	IElementType NEON_INDENT = new NeonTokenType("indent");
	IElementType NEON_LITERAL = new NeonTokenType("literal");
	IElementType NEON_KEYWORD = new NeonTokenType("keyword");
	IElementType NEON_KEY_USAGE = new NeonTokenType("keyUsage");
	IElementType NEON_PARAMETER_USAGE = new NeonTokenType("parameterUsage");
	IElementType NEON_WHITESPACE = TokenType.WHITE_SPACE; // new NeonTokenType("whitespace");
	IElementType NEON_UNKNOWN = TokenType.BAD_CHARACTER; // new NeonTokenType("error");

	// symbols
	IElementType NEON_COLON = new NeonTokenType(":");
	IElementType NEON_ASSIGNMENT = new NeonTokenType("=");
	IElementType NEON_ARRAY_BULLET = new NeonTokenType("-");
	IElementType NEON_ITEM_DELIMITER = new NeonTokenType(",");

	// braces
	IElementType NEON_LPAREN = new NeonTokenType("(");
	IElementType NEON_RPAREN = new NeonTokenType(")");
	IElementType NEON_LBRACE_CURLY = new NeonTokenType("{");
	IElementType NEON_RBRACE_CURLY = new NeonTokenType("}");
	IElementType NEON_LBRACE_SQUARE = new NeonTokenType("[");
	IElementType NEON_RBRACE_SQUARE = new NeonTokenType("]");


	// the rest are deprecated and will be removed
	IElementType NEON_IDENTIFIER = new NeonTokenType("identifier");
	IElementType NEON_EOL = new NeonTokenType("eol");
	IElementType NEON_VARIABLE = new NeonTokenType("variable");
	IElementType NEON_NUMBER = new NeonTokenType("number");
	IElementType NEON_REFERENCE = new NeonTokenType("reference");
	IElementType NEON_BLOCK_INHERITENCE = new NeonTokenType("<");
	IElementType NEON_DOUBLE_COLON = new NeonTokenType("::");
	IElementType NEON_DOLLAR = new NeonTokenType("$");
	IElementType NEON_AT = new NeonTokenType("@");


	// special tokens (identifier in block header or as array key)
	IElementType NEON_KEY = new NeonTokenType("key");


	// sets
	TokenSet WHITESPACES = TokenSet.create(NEON_WHITESPACE);
	TokenSet COMMENTS = TokenSet.create(NEON_COMMENT);
	TokenSet STRING_LITERALS = TokenSet.create(
			NEON_LITERAL, NEON_STRING, NEON_NUMBER, NEON_CLASS_NAME, NEON_METHOD, NEON_DOUBLE_COLON,
			NEON_KEY_IDENTIFIER, NEON_PHP_STATIC_IDENTIFIER, NEON_KEY_USAGE, NEON_PARAMETER_USAGE
	);
	TokenSet ASSIGNMENTS = TokenSet.create(NEON_ASSIGNMENT, NEON_COLON);
	TokenSet OPEN_BRACKET = TokenSet.create(NEON_LPAREN, NEON_LBRACE_CURLY, NEON_LBRACE_SQUARE);
	TokenSet OPEN_QUOTE = TokenSet.create(NEON_SINGLE_QUOTE_LEFT, NEON_DOUBLE_QUOTE_LEFT);
	TokenSet CLOSING_BRACKET = TokenSet.create(NEON_RPAREN, NEON_RBRACE_CURLY, NEON_RBRACE_SQUARE);
	TokenSet INLINE_ARRAY_ITEMS = TokenSet.create(
			NEON_INDENT, NEON_ITEM_DELIMITER, NEON_NUMBER, NEON_CLASS_NAME, NEON_DOUBLE_COLON,
			NEON_KEY_IDENTIFIER, NEON_PHP_STATIC_IDENTIFIER, NEON_KEY_USAGE, NEON_PARAMETER_USAGE
	);
	TokenSet SYMBOLS = TokenSet.create(
		NEON_COLON, NEON_ASSIGNMENT, NEON_ARRAY_BULLET, NEON_ITEM_DELIMITER,
		NEON_LPAREN, NEON_RPAREN,
		NEON_LBRACE_CURLY, NEON_RBRACE_CURLY,
		NEON_LBRACE_SQUARE, NEON_RBRACE_SQUARE
	);

	// brackets
	public static final Map<IElementType, IElementType> closingBrackets = ImmutableMap.of(
		NEON_LPAREN, NEON_RPAREN,
		NEON_LBRACE_CURLY, NEON_RBRACE_CURLY,
		NEON_LBRACE_SQUARE, NEON_RBRACE_SQUARE
	);

}
