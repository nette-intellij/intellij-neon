package cz.juzna.intellij.neon;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.UsefulTestCase;
import com.sun.tools.javac.util.Pair;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

abstract public class BaseLexerTestCase extends UsefulTestCase {

	final private @NotNull String resourceDataPath;

	protected BaseLexerTestCase(@NotNull String fullDataPath) {
		super();
		this.resourceDataPath = fullDataPath;
	}

	abstract protected MergingLexerAdapter createLexer();

	/**
	 * Test that lexing a given piece of code will give particular tokens
	 *
	 * @param text Sample piece of neon code
	 * @param expectedTokens List of tokens expected from lexer
	 */
	protected void doTest(@NonNls String text, @NonNls Pair<IElementType, String>[] expectedTokens) {
		Lexer lexer = createLexer();
		doTest(text, expectedTokens, lexer);
	}

	private void doTest(String text, Pair<IElementType, String>[] expectedTokens, Lexer lexer) {
		lexer.start(text);
		int idx = 0;
		while (lexer.getTokenType() != null) {
			if (idx >= expectedTokens.length) fail("Too many tokens from lexer; unexpected " + lexer.getTokenType());

			Pair<IElementType, String> expected = expectedTokens[idx++];
			assertEquals("Wrong token", expected.fst, lexer.getTokenType());

			String tokenText = lexer.getBufferSequence().subSequence(lexer.getTokenStart(), lexer.getTokenEnd()).toString();
			assertEquals(expected.snd, tokenText);
			lexer.advance();
		}

		if (idx < expectedTokens.length) fail("Not enough tokens from lexer, expected " + expectedTokens.length + " but got only " + idx);
	}

	public void doTestFromFile() throws Exception {
		String code = doLoadFile(resourceDataPath, getTestName(false) + ".neon");

		Lexer lexer = createLexer();
		StringBuilder sb = new StringBuilder();

		lexer.start(code);
		while (lexer.getTokenType() != null) {
			sb.append(lexer.getTokenType().toString());
			sb.append("\n");
			lexer.advance();
		}

//		System.out.println(sb);

		// Match to original
		String lexed = doLoadFile(resourceDataPath, getTestName(false) + ".lexed");
		assertEquals(lexed, sb.toString());
	}

	private String doLoadFile(String myResourcePath, String name) throws IOException {
		String path = myResourcePath + name;
		URL url = getClass().getClassLoader().getResource(path);
		if (url == null) {
			throw new AssertionError("Source file " + path + " not found");
		}
		String text = FileUtil.loadFile(new File(url.getFile()), CharsetToolkit.UTF8);
		text = StringUtil.convertLineSeparators(text);
		return text;
	}
}
