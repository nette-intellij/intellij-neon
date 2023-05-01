package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.MergingLexerAdapter;
import cz.juzna.intellij.neon.BaseLexerTestCase;
import org.junit.Test;

public class LexerTokenTest extends BaseLexerTestCase {
	public LexerTokenTest() {
		super("data/psi/neonPsi/");
	}

	// which lexer to test
	protected MergingLexerAdapter createLexer() {
		return new NeonLexer();
	}

	@Test
	public void testDefault() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testDefault1() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testDefault2() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testNested() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testNested1() throws Exception {
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
	public void testArray7() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArray8() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testArray9() throws Exception {
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
	public void testMultiline() throws Exception {
		doTestFromFile();
	}

	@Test
	public void testItemValueAfterNewLine() throws Exception {
		doTestFromFile();
	}

}
