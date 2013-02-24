package cz.juzna.intellij.neon.lexer;

import com.intellij.testFramework.UsefulTestCase;
import org.junit.Test;

/**
 *
 */
public class NeonLexer2Test extends UsefulTestCase {

	@Test
	public void test01() {
		NeonLexer2 l = new NeonLexer2();
		l.start("key: val");
		l.advance();

		assertEquals(NeonTokenTypes.NEON_LITERAL, l.getTokenType());
		assertEquals(0, l.getTokenStart());
		assertEquals(2, l.getTokenEnd());
		assertEquals("key", l.getTokenText());
	}
}
