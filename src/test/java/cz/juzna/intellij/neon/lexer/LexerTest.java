package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.MergingLexerAdapter;
import com.sun.tools.javac.util.Pair;
import cz.juzna.intellij.neon.BaseLexerTestCase;
import org.junit.Test;

import static cz.juzna.intellij.neon.lexer.NeonTokenTypes.*;

public class LexerTest extends BaseLexerTestCase {
	public LexerTest() {
		super("src/test/data/parser");
	}

	// which lexer to test
	protected MergingLexerAdapter createLexer() {
		return new NeonLexer();
	}

	/*** tests here ***/

	@Test
	public void testSimple() throws Exception {
		doTest("name: 'Jan'", new Pair[] {
				Pair.of(NEON_LITERAL, "name"),
				Pair.of(NEON_COLON, ":"),
				Pair.of(NEON_WHITESPACE, " "),
				Pair.of(NEON_STRING, "'Jan'"),
		});
	}

	@Test
	public void testTabAfterKey() throws Exception {
		doTest("name: \t'Jan'\nsurname:\t \t 'Dolecek'", new Pair[] {
				Pair.of(NEON_LITERAL, "name"),
				Pair.of(NEON_COLON, ":"),
				Pair.of(NEON_WHITESPACE, " \t"),
				Pair.of(NEON_STRING, "'Jan'"),
				Pair.of(NEON_INDENT, "\n"),
				Pair.of(NEON_LITERAL, "surname"),
				Pair.of(NEON_COLON, ":"),
				Pair.of(NEON_WHITESPACE, "\t \t "),
				Pair.of(NEON_STRING, "'Dolecek'"),
		});
	}

	@Test
	public void testDefault() throws Exception {
		doTestFromFile();
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

}
