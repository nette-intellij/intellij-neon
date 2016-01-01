package cz.juzna.intellij.neon.parser;

import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.PlatformTestCase;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest extends ParsingTestCase {

	public ParserTest() {
		super("", "neon", new NeonParserDefinition());
		PlatformTestCase.initPlatformLangPrefix();
	}

	@Override
	protected String getTestDataPath() {
		return "src/test/data/parser";
	}

	protected void doTest(boolean checkResult, boolean suppressErrors) {
		doTest(true);
		if (!suppressErrors) {
			Assert.assertFalse(
				"PsiFile contains error elements",
				toParseTreeText(myFile, true, includeRanges()).contains("PsiErrorElement")
			);
		}
	}

	@Test
	public void test01() {
		doTest(true, false);
	}

	@Test
	public void test02() {
		doTest(true, false);
	}

	@Test
	public void test03() {
		doTest(true, false);
	}

	@Test
	public void test04() {
		doTest(true, false);
	}

	@Test
	public void test05() {
		doTest(true, false);
	}

	@Test
	public void test06() {
		doTest(true, false);
	}

	@Test
	public void test07() {
		doTest(true, false);
	}

	@Test
	public void test08() {
		doTest(true, false);
	}

	@Test
	public void test09() {
		doTest(true, false);
	}

	@Test
	public void test10() {
		doTest(true, false);
	}

	@Test
	public void test11() {
		doTest(true, false);
	}

	@Test
	public void test12() {
		doTest(true, true);
	}

	@Test
	public void test13() {
		doTest(true, true);
	}

	@Test
	public void test14() {
		doTest(true, true);
	}

	@Test
	public void test15() {
		doTest(true, true);
	}

	@Test
	public void test16() {
		doTest(true, true);
	}

	@Test
	public void test17() {
		doTest(true, true);
	}

	@Test
	public void test18() {
		doTest(true, true);
	}

	@Test
	public void test19() {
		doTest(true, true);
	}

	@Test
	public void test20() {
		doTest(true, true);
	}

	@Test
	public void test21() {
		doTest(true, true);
	}

	@Test
	public void test22() {
		doTest(true, true);
	}

	@Test
	public void test23() {
		doTest(true, true);
	}

	@Test
	public void test24() {
		doTest(true, true);
	}

	@Test
	public void test25() {
		doTest(true, true);
	}

	@Test
	public void test26() {
		doTest(true, true);
	}

	@Test
	public void test27() {
		doTest(true, true);
	}

	@Test
	public void test28() {
		doTest(true, true);
	}

	@Test
	public void test29() {
		doTest(true, true);
	}

	@Test
	public void test30() {
		doTest(true, true);
	}

	@Test
	public void test31() {
		doTest(true, true);
	}

	@Test
	public void test32() {
		doTest(true, true);
	}

}
