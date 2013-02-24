package cz.juzna.intellij.neon.parser;

import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.PlatformTestCase;
import org.junit.Test;
import org.junit.Assert;

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

}
