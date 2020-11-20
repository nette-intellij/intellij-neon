package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.Lexer;
import org.junit.Assert;
import org.junit.Test;

public class NeonHighlightingLexerTest {

	@Test
	public void testKeys() {
		Lexer l = new NeonHighlightingLexer(new NeonLexer());
		l.start("key: val");

		Assert.assertEquals(NeonTokenTypes.NEON_KEYWORD, l.getTokenType()); // this is important
		Assert.assertEquals(0, l.getTokenStart());
		Assert.assertEquals(3, l.getTokenEnd());
		Assert.assertEquals("key", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_COLON, l.getTokenType());
		Assert.assertEquals(3, l.getTokenStart());
		Assert.assertEquals(4, l.getTokenEnd());
		Assert.assertEquals(":", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_WHITESPACE, l.getTokenType());
		Assert.assertEquals(4, l.getTokenStart());
		Assert.assertEquals(5, l.getTokenEnd());
		Assert.assertEquals(" ", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_IDENTIFIER, l.getTokenType());
		Assert.assertEquals(5, l.getTokenStart());
		Assert.assertEquals(8, l.getTokenEnd());
		Assert.assertEquals("val", l.getTokenText());
		l.advance();

		Assert.assertNull(l.getTokenType());
	}

	@Test
	public void testKeywords() {
		Lexer l = new NeonHighlightingLexer(new NeonLexer());
		l.start("[true,off,TruE,\"true\"]");

		Assert.assertEquals(NeonTokenTypes.NEON_LBRACE_SQUARE, l.getTokenType()); // this is important
		Assert.assertEquals(0, l.getTokenStart());
		Assert.assertEquals(1, l.getTokenEnd());
		Assert.assertEquals("[", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_KEYWORD, l.getTokenType());
		Assert.assertEquals(1, l.getTokenStart());
		Assert.assertEquals(5, l.getTokenEnd());
		Assert.assertEquals("true", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_ITEM_DELIMITER, l.getTokenType());
		Assert.assertEquals(5, l.getTokenStart());
		Assert.assertEquals(6, l.getTokenEnd());
		Assert.assertEquals(",", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_IDENTIFIER, l.getTokenType());
		Assert.assertEquals(6, l.getTokenStart());
		Assert.assertEquals(9, l.getTokenEnd());
		Assert.assertEquals("off", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_ITEM_DELIMITER, l.getTokenType());
		Assert.assertEquals(9, l.getTokenStart());
		Assert.assertEquals(10, l.getTokenEnd());
		Assert.assertEquals(",", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_IDENTIFIER, l.getTokenType());
		Assert.assertEquals(10, l.getTokenStart());
		Assert.assertEquals(14, l.getTokenEnd());
		Assert.assertEquals("TruE", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_ITEM_DELIMITER, l.getTokenType());
		Assert.assertEquals(14, l.getTokenStart());
		Assert.assertEquals(15, l.getTokenEnd());
		Assert.assertEquals(",", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_DOUBLE_QUOTE_LEFT, l.getTokenType());
		Assert.assertEquals(15, l.getTokenStart());
		Assert.assertEquals(16, l.getTokenEnd());
		Assert.assertEquals("\"", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_STRING, l.getTokenType());
		Assert.assertEquals(16, l.getTokenStart());
		Assert.assertEquals(20, l.getTokenEnd());
		Assert.assertEquals("true", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_DOUBLE_QUOTE_RIGHT, l.getTokenType());
		Assert.assertEquals(20, l.getTokenStart());
		Assert.assertEquals(21, l.getTokenEnd());
		Assert.assertEquals("\"", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_RBRACE_SQUARE, l.getTokenType());
		Assert.assertEquals(21, l.getTokenStart());
		Assert.assertEquals(22, l.getTokenEnd());
		Assert.assertEquals("]", l.getTokenText());
		l.advance();

		Assert.assertNull(l.getTokenType());
	}
}
