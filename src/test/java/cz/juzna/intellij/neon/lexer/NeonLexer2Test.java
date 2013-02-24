package cz.juzna.intellij.neon.lexer;

import com.intellij.testFramework.UsefulTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class NeonLexer2Test extends UsefulTestCase {

	@Test
	public void test01() {
		NeonLexer2 l = new NeonLexer2();
		l.start("key: 'val'");

		l.advance();
		assertEquals(NeonTokenTypes.NEON_LITERAL, l.getTokenType());
		assertEquals(0, l.getTokenStart());
		assertEquals(3, l.getTokenEnd());
		assertEquals("key", l.getTokenText());

		l.advance();
		assertEquals(NeonTokenTypes.NEON_SYMBOL, l.getTokenType());
		assertEquals(3, l.getTokenStart());
		assertEquals(4, l.getTokenEnd());
		assertEquals(":", l.getTokenText());

		l.advance();
		assertEquals(NeonTokenTypes.NEON_WHITESPACE, l.getTokenType());
		assertEquals(4, l.getTokenStart());
		assertEquals(5, l.getTokenEnd());
		assertEquals(" ", l.getTokenText());

		l.advance();
		assertEquals(NeonTokenTypes.NEON_STRING, l.getTokenType());
		assertEquals(5, l.getTokenStart());
		assertEquals(10, l.getTokenEnd());
		assertEquals("'val'", l.getTokenText());

		l.advance();
		assertEquals(null, l.getTokenType());
	}
}
