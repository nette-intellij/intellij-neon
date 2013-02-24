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
	public void test01() {
		Lexer l = new NeonHighlightingLexer(new NeonLexer2());
		l.start("key: val");

		assertEquals(NeonTokenTypes.NEON_KEY, l.getTokenType()); // this is important
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

		assertEquals(NeonTokenTypes.NEON_LITERAL, l.getTokenType());
		assertEquals(5, l.getTokenStart());
		assertEquals(8, l.getTokenEnd());
		assertEquals("val", l.getTokenText());
		l.advance();

		assertEquals(null, l.getTokenType());
	}
}
