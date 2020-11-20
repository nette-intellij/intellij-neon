package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.Lexer;
import org.junit.Assert;
import org.junit.Test;

public class NeonLexer2Test {

	@Test
	public void test01() {
		Lexer l = createLexer();
		l.start("key: 'val'");

		Assert.assertEquals(NeonTokenTypes.NEON_IDENTIFIER, l.getTokenType());
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

		Assert.assertEquals(NeonTokenTypes.NEON_SINGLE_QUOTE_LEFT, l.getTokenType());
		Assert.assertEquals(5, l.getTokenStart());
		Assert.assertEquals(6, l.getTokenEnd());
		Assert.assertEquals("'", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_STRING, l.getTokenType());
		Assert.assertEquals(6, l.getTokenStart());
		Assert.assertEquals(9, l.getTokenEnd());
		Assert.assertEquals("val", l.getTokenText());
		l.advance();

		Assert.assertEquals(NeonTokenTypes.NEON_SINGLE_QUOTE_RIGHT, l.getTokenType());
		Assert.assertEquals(9, l.getTokenStart());
		Assert.assertEquals(10, l.getTokenEnd());
		Assert.assertEquals("'", l.getTokenText());
		l.advance();

		Assert.assertNull(l.getTokenType());
	}

	@Test
	public void test02() {
		Lexer l = createLexer();
		l.start("key: 'val'", 4, 5);

		Assert.assertEquals(NeonTokenTypes.NEON_INDENT, l.getTokenType());
		Assert.assertEquals(4, l.getTokenStart());
		Assert.assertEquals(5, l.getTokenEnd());
		Assert.assertEquals(" ", l.getTokenText());
		l.advance();

		Assert.assertNull(l.getTokenType());
	}

	private Lexer createLexer() {
		return new NeonLexer();
	}

}
