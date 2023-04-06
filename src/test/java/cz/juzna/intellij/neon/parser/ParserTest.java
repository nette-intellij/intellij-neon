package cz.juzna.intellij.neon.parser;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.TestDataFile;
import org.jetbrains.annotations.NonNls;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ParserTest extends ParsingTestCase {

	public ParserTest() {
		super("", "neon", new NeonParserDefinition());
		//PlatformTestCase.doAutodetectPlatformPrefix();
	}

	@Override
	protected String getTestDataPath() {
		return "src/test/data/parser";
	}

	protected void doParserTest(boolean checkResult, boolean suppressErrors) {
		doTest(true);
		if (!suppressErrors) {
			Assert.assertFalse(
					"PsiFile contains error elements",
					toParseTreeText(myFile, true, includeRanges()).contains("PsiErrorElement")
			);
		}
	}

	protected String loadFile(@NonNls @TestDataFile String name) throws IOException {
		return FileUtil.loadFile(new File(myFullDataPath, name), CharsetToolkit.UTF8, true);
	}

	@Test
	public void testArray1() {
		doParserTest(true, false);
	}

	@Test
	public void testArray2() {
		doParserTest(true, false);
	}

	@Test
	public void testArray3() {
		doParserTest(true, false);
	}

	@Test
	public void testArray4() {
		doParserTest(true, false);
	}

	@Test
	public void testArray5() {
		doParserTest(true, false);
	}

	@Test
	public void testArray6() {
		doParserTest(true, false);
	}

	@Test
	public void testArray7() {
		doParserTest(true, false);
	}

	@Test
	public void testArray8() {
		doParserTest(true, false);
	}

	@Test
	public void testArray9() {
		doParserTest(true, false);
	}

	@Test
	public void testArray10() {
		doParserTest(true, false);
	}

	@Test
	public void testArrayComment() {
		doParserTest(true, false);
	}

	@Test
	public void testArrayEntity() {
		doParserTest(true, false);
	}

	@Test
	public void testArrayEntity2() {
		doParserTest(true, false);
	}

	@Test
	public void testArrayInline() {
		doParserTest(true, false);
	}

	@Test
	public void testArrayInline2() {
		doParserTest(true, false);
	}

	@Test
	public void testArrayInline3() {
		doParserTest(true, false);
	}

	@Test
	public void testReal1() {
		doParserTest(true, false);
	}

	@Test
	public void testArrayAfterKey() {
		doParserTest(true, false);
	}

	@Test
	public void testEntityChain() {
		doParserTest(true, false);
	}

	@Test
	public void testEntityArrayValue() {
		doParserTest(true, false);
	}

	@Test
	public void testTabSpaceMixing() {
		doParserTest(true, false);
	}

	@Test
	public void testKeyAfterBullet1() {
		doParserTest(true, false);
	}

	@Test
	public void testKeyAfterBulletArrayAfterKey() {
		doParserTest(true, false);
	}

	@Test
	public void testKeyAfterBullet2() {
		doParserTest(true, false);
	}

	@Test
	public void testKeyAfterBullet3() {
		doParserTest(true, false);
	}

	@Test
	public void testKeyAfterBulletFalse() {
		doParserTest(true, false);
	}

	@Test
	public void testArrayNoSpaceColon() {
		doParserTest(true, false);
	}

	@Test
	public void testArrayNull() {
		doParserTest(true, false);
	}

	@Test
	public void testArrayIndentedFile() {
		doParserTest(true, false);
	}

	@Test
	public void testItemValueAfterNewLine() {
		doParserTest(true, false);
	}

	@Test
	public void testEmptyLineBeginning() {
		doParserTest(true, false);
	}

	@Test
	public void testErrorInlineArray() {
		doParserTest(true, true);
	}

	@Test
	public void testErrorClosingBracket2() {
		doParserTest(true, true);
	}

	@Test
	public void testErrorClosingBracket() {
		doParserTest(true, true);
	}

	@Test
	public void testErrorTabSpaceMixing() {
		doParserTest(true, true);
	}

	@Test
	public void testErrorIndent() {
		doParserTest(true, true);
	}

}
