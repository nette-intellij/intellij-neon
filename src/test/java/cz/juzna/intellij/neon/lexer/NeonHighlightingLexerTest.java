package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.Lexer;
import com.intellij.testFramework.UsefulTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class NeonHighlightingLexerTest extends UsefulTestCase {

	@Test
	public void testKeys() {
		Lexer l = new NeonHighlightingLexer(new NeonLexer());
		l.start("key: val");

		assertEquals(NeonTokenTypes.NEON_KEY, l.getTokenType()); // this is important
		assertEquals(0, l.getTokenStart());
		assertEquals(3, l.getTokenEnd());
		assertEquals("key", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_COLON, l.getTokenType());
		assertEquals(3, l.getTokenStart());
		assertEquals(4, l.getTokenEnd());
		assertEquals(":", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_WHITESPACE, l.getTokenType());
		assertEquals(4, l.getTokenStart());
		assertEquals(5, l.getTokenEnd());
		assertEquals(" ", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_LITERAL, l.getTokenType());
		assertEquals(5, l.getTokenStart());
		assertEquals(8, l.getTokenEnd());
		assertEquals("val", l.getTokenText());
		l.advance();

		assertEquals(null, l.getTokenType());
	}

	@Test
	public void testKeywords() {
		Lexer l = new NeonHighlightingLexer(new NeonLexer());
		l.start("[true,off,TruE,\"true\"]");

		assertEquals(NeonTokenTypes.NEON_LBRACE_SQUARE, l.getTokenType()); // this is important
		assertEquals(0, l.getTokenStart());
		assertEquals(1, l.getTokenEnd());
		assertEquals("[", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_KEYWORD, l.getTokenType());
		assertEquals(1, l.getTokenStart());
		assertEquals(5, l.getTokenEnd());
		assertEquals("true", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_ITEM_DELIMITER, l.getTokenType());
		assertEquals(5, l.getTokenStart());
		assertEquals(6, l.getTokenEnd());
		assertEquals(",", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_KEYWORD, l.getTokenType());
		assertEquals(6, l.getTokenStart());
		assertEquals(9, l.getTokenEnd());
		assertEquals("off", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_ITEM_DELIMITER, l.getTokenType());
		assertEquals(9, l.getTokenStart());
		assertEquals(10, l.getTokenEnd());
		assertEquals(",", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_LITERAL, l.getTokenType());
		assertEquals(10, l.getTokenStart());
		assertEquals(14, l.getTokenEnd());
		assertEquals("TruE", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_ITEM_DELIMITER, l.getTokenType());
		assertEquals(14, l.getTokenStart());
		assertEquals(15, l.getTokenEnd());
		assertEquals(",", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_STRING, l.getTokenType());
		assertEquals(15, l.getTokenStart());
		assertEquals(21, l.getTokenEnd());
		assertEquals("\"true\"", l.getTokenText());
		l.advance();

		assertEquals(NeonTokenTypes.NEON_RBRACE_SQUARE, l.getTokenType());
		assertEquals(21, l.getTokenStart());
		assertEquals(22, l.getTokenEnd());
		assertEquals("]", l.getTokenText());
		l.advance();

		assertEquals(null, l.getTokenType());
	}
}
