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
	public void testArray1() {
		doTest(true, false);
	}

	@Test
	public void testArray2() {
		doTest(true, false);
	}

	@Test
	public void testArray3() {
		doTest(true, false);
	}

	@Test
	public void testArray4() {
		doTest(true, false);
	}

	@Test
	public void testArray5() {
		doTest(true, false);
	}

	@Test
	public void testArray6() {
		doTest(true, false);
	}

	@Test
	public void testArray7() {
		doTest(true, false);
	}

	@Test
	public void testArray8() {
		doTest(true, false);
	}

	@Test
	public void testArray9() {
		doTest(true, false);
	}

	@Test
	public void testArray10() {
		doTest(true, false);
	}

	@Test
	public void testArrayComment() {
		doTest(true, false);
	}

	@Test
	public void testArrayEntity() {
		doTest(true, false);
	}

	@Test
	public void testArrayEntity2() {
		doTest(true, false);
	}

	@Test
	public void testArrayInline() {
		doTest(true, false);
	}

	@Test
	public void testArrayInline2() {
		doTest(true, false);
	}

	@Test
	public void testArrayInline3() {
		doTest(true, false);
	}

	@Test
	public void tesArrayNull() {
		doTest(true, false);
	}


	@Test
	public void testReal1() {
		doTest(true, false);
	}


	@Test
	public void testArrayAfterKey() {
		doTest(true, false);
	}

	@Test
	public void testEntityChain() {
		doTest(true, false);
	}

	@Test
	public void testEntityArrayValue() {
		doTest(true, false);
	}

	@Test
	public void testTabSpaceMixing() {
		doTest(true, false);
	}


	@Test
	public void testKeyAfterBullet1() {
		doTest(true, false);
	}

	@Test
	public void testKeyAfterBulletArrayAfterKey() {
		doTest(true, false);
	}

	@Test
	public void testKeyAfterBullet2() {
		doTest(true, false);
	}

	@Test
	public void testKeyAfterBulletFalse() {
		doTest(true, false);
	}

	@Test
	public void testArrayNoSpaceColon() {
		doTest(true, false);
	}

	@Test
	public void testArrayNull() {
		doTest(true, false);
	}

	@Test
	public void testErrorNoSpaceColon() {
		doTest(true, true);
	}

	@Test
	public void testErrorInlineArray() {
		doTest(true, true);
	}

	@Test
	public void testErrorClosingBracket2() {
		doTest(true, true);
	}

	@Test
	public void testErrorClosingBracket() {
		doTest(true, true);
	}

	@Test
	public void testErrorTabSpaceMixing() {
		doTest(true, true);
	}

	@Test
	public void testErrorIndent() {
		doTest(true, true);
	}

}
