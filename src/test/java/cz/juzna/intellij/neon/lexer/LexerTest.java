package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.application.PluginPathManager;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.LightPlatformTestCase;
import com.intellij.testFramework.PlatformLiteFixture;
import com.intellij.testFramework.PlatformTestCase;
import com.sun.tools.javac.util.Pair;
import cz.juzna.intellij.neon.parser.NeonElementType;
import org.jetbrains.annotations.NonNls;
import org.testng.annotations.*;

import static junit.framework.Assert.*;
import static cz.juzna.intellij.neon.lexer.NeonTokenTypes.*;

/**
 *
 */
public class LexerTest extends LightPlatformTestCase {
	public LexerTest() {
		PlatformTestCase.initPlatformLangPrefix();
	}


	/*** helpers ***/

	/**
	 * Test that lexing a given piece of code will give particular tokens
	 *
	 * @param text Sample piece of neon code
	 * @param expectedTokens List of tokens expected from lexer
	 */
	protected static void doTest(@NonNls String text, @NonNls Pair<IElementType, String>[] expectedTokens) {
		Lexer lexer = new NeonLexer();
		doTest(text, expectedTokens, lexer);
	}

	private static void doTest(String text, Pair<IElementType, String>[] expectedTokens, Lexer lexer) {
		lexer.start(text);
		int idx = 0;
		while (lexer.getTokenType() != null) {
			if (idx >= expectedTokens.length) fail("Too many tokens from lexer; unexpected " + lexer.getTokenType());

			Pair expected = expectedTokens[idx++];
			assertEquals("Wrong token", expected.fst, lexer.getTokenType());

			String tokenText = lexer.getBufferSequence().subSequence(lexer.getTokenStart(), lexer.getTokenEnd()).toString();
			assertEquals(expected.snd, tokenText);
			lexer.advance();
		}

		if (idx < expectedTokens.length) fail("Not enough tokens from lexer");
	}



	/*** tests here ***/

	@org.testng.annotations.Test
	public void testSimple() throws Exception {
		doTest("name: Jan", new Pair[] {
				Pair.of(NEON_KEY, "name"),
				Pair.of(NEON_ASSIGNMENT, ":"),
				Pair.of(NEON_WHITESPACE, " "),
				Pair.of(NEON_IDENTIFIER, "Jan"),
		});
	}

}
