package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.tree.IElementType;
import cz.juzna.intellij.neon.util.Pair;
import org.jetbrains.annotations.NonNls;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static cz.juzna.intellij.neon.lexer.NeonTokenTypes.*;

/**
 *
 */
public class LexerTest {
	@Rule
	public TestName name = new TestName();

	// which lexer to test
	private static NeonLexer createLexer() {
		return new NeonLexer();
	}

	/**
	 * Test that lexing a given piece of code will give particular tokens
	 *
	 * @param text Sample piece of neon code
	 * @param expectedTokens List of tokens expected from lexer
	 */
	protected static void doTest(@NonNls String text, @NonNls Pair<IElementType, String>[] expectedTokens) {
		Lexer lexer = createLexer();
		doTest(text, expectedTokens, lexer);
	}

	private static void doTest(String text, Pair<IElementType, String>[] expectedTokens, Lexer lexer) {
		lexer.start(text);
		int idx = 0;
		while (lexer.getTokenType() != null) {
			if (idx >= expectedTokens.length) Assert.fail("Too many tokens from lexer; unexpected " + lexer.getTokenType());

			Pair<IElementType, String> expected = expectedTokens[idx++];
			Assert.assertEquals("Wrong token", expected.first, lexer.getTokenType());

			String tokenText = lexer.getBufferSequence().subSequence(lexer.getTokenStart(), lexer.getTokenEnd()).toString();
			Assert.assertEquals(expected.second, tokenText);
			lexer.advance();
		}

		if (idx < expectedTokens.length) Assert.fail("Not enough tokens from lexer, expected " + expectedTokens.length + " but got only " + idx);
	}



	/*** tests here ***/

	@Test
	public void testSimple() throws Exception {
		doTest("name: 'Jan'", new Pair[] {
				Pair.of(NEON_IDENTIFIER, "name"),
				Pair.of(NEON_COLON, ":"),
				Pair.of(NEON_WHITESPACE, " "),
				Pair.of(NEON_SINGLE_QUOTE_LEFT, "'"),
				Pair.of(NEON_STRING, "Jan"),
				Pair.of(NEON_SINGLE_QUOTE_RIGHT, "'"),
		});
	}

	@Test
	public void testTabAfterKey() throws Exception {
		doTest("name: \t'Jan'\nsurname:\t \t 'Dolecek'", new Pair[] {
				Pair.of(NEON_IDENTIFIER, "name"),
				Pair.of(NEON_COLON, ":"),
				Pair.of(NEON_WHITESPACE, " \t"),
				Pair.of(NEON_SINGLE_QUOTE_LEFT, "'"),
				Pair.of(NEON_STRING, "Jan"),
				Pair.of(NEON_SINGLE_QUOTE_RIGHT, "'"),
				Pair.of(NEON_INDENT, "\n"),
				Pair.of(NEON_IDENTIFIER, "surname"),
				Pair.of(NEON_COLON, ":"),
				Pair.of(NEON_WHITESPACE, "\t \t "),
				Pair.of(NEON_SINGLE_QUOTE_LEFT, "'"),
				Pair.of(NEON_STRING, "Dolecek"),
				Pair.of(NEON_SINGLE_QUOTE_RIGHT, "'"),
		});
	}

	@Test
	public void testArray1() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArray2() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArray3() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArray4() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArray5() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArray6() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArray10() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArrayEntity() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArrayIndentedFile() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArrayNoSpaceColon() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testMultiline1() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArrayLastElement() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testParameterNoUsage() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testObjectParameters() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testObjectParametersDelimiter() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testKeyDuplicity() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testStringInApostrophe() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testStringInDoubleQuote() throws Exception {
		doTestFromFile();
	}

	public void doTestFromFile() throws Exception {
		String code = doLoadFile(name.getMethodName() + ".neon");

		Lexer lexer = createLexer();
		StringBuilder sb = new StringBuilder();

		lexer.start(code);
		while (lexer.getTokenType() != null) {
			sb.append(lexer.getTokenType().toString());
			sb.append("\n");
			lexer.advance();
		}

		// Match to original
		String lexed = doLoadFile(name.getMethodName() + ".lexed");
		Assert.assertEquals(lexed, sb.toString());
	}

	private static String doLoadFile(String name) throws IOException {
		URL url = LexerTest.class.getClassLoader().getResource("data/parser" + File.separatorChar + (normalizeMethodName(name)));
		assert url != null;
		String text = FileUtil.loadFile(new File(url.getFile()), CharsetToolkit.UTF8);
		text = StringUtil.convertLineSeparators(text);
		return text;
	}

	private static String normalizeMethodName(String name) {
		return name.startsWith("test") ? name.substring(4) : name;
	}

}
