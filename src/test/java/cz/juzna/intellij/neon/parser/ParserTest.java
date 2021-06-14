package cz.juzna.intellij.neon.parser;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.testFramework.HeavyPlatformTestCase;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.TestDataFile;
import org.jetbrains.annotations.NonNls;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ParserTest extends ParsingTestCase {

	public ParserTest() {
		super("", "neon", new NeonParserDefinition());
		HeavyPlatformTestCase.doAutodetectPlatformPrefix();
	}

	@Override
	protected String getTestDataPath() {
		URL url = getClass().getClassLoader().getResource("data/parser");
		assert url != null;
		return url.getFile();
	}
/*
	protected void doTest(boolean checkResult, boolean suppressErrors) {
		doTest(true);
		if (!suppressErrors) {
			Assert.assertFalse(
					"PsiFile contains error elements",
					toParseTreeText(myFile, true, includeRanges()).contains("PsiErrorElement")
			);
		}
	}
*/
	protected String loadFile(@NonNls @TestDataFile String name) throws IOException {
		return FileUtil.loadFile(new File(myFullDataPath, name), CharsetToolkit.UTF8, true);
	}

	@Test
	public void testArray1() {
		doTest(true, true);
	}

	@Test
	public void testArray2() {
		doTest(true, true);
	}

	@Test
	public void testArray3() {
		doTest(true, true);
	}

	@Test
	public void testArray4() {
		doTest(true, true);
	}

	@Test
	public void testArray5() {
		doTest(true, true);
	}

	@Test
	public void testArray6() {
		doTest(true, true);
	}

	@Test
	public void testArray7() {
		doTest(true, true);
	}

	@Test
	public void testArray8() {
		doTest(true, true);
	}

	@Test
	public void testArray9() {
		doTest(true, true);
	}

	@Test
	public void testArray10() {
		doTest(true, true);
	}

	@Test
	public void testArrayComment() {
		doTest(true, true);
	}

	@Test
	public void testArrayEntity() {
		doTest(true, true);
	}

	@Test
	public void testArrayEntity2() {
		doTest(true, true);
	}

	@Test
	public void testArrayInline() {
		doTest(true, true);
	}

	@Test
	public void testArrayLastElement() {
		doTest(true, true);
	}

	@Test
	public void testArrayInline2() {
		doTest(true, true);
	}

	@Test
	public void testArrayInline3() {
		doTest(true, true);
	}

	@Test
	public void testReal1() {
		doTest(true, true);
	}

	@Test
	public void testArrayAfterKey() {
		doTest(true, true);
	}

	@Test
	public void testEntityChain() {
		doTest(true, true);
	}

	@Test
	public void testEntityArrayValue() {
		doTest(true, true);
	}

	@Test
	public void testTabSpaceMixing() {
		doTest(true, true);
	}

	@Test
	public void testKeyAfterBullet1() {
		doTest(true, true);
	}

	@Test
	public void testKeyAfterBulletArrayAfterKey() {
		doTest(true, true);
	}

	@Test
	public void testKeyAfterBullet2() {
		doTest(true, true); //todo: fix IT
	}

	@Test
	public void testKeyAfterBullet3() {
		doTest(true, true);
	}

	@Test
	public void testKeyAfterBulletFalse() {
		doTest(true, true);
	}

	@Test
	public void testArrayNoSpaceColon() {
		doTest(true, true);
	}

	@Test
	public void testArrayNull() {
		doTest(true, true);
	}

	@Test
	public void testArrayIndentedFile() {
		doTest(true, true);
	}

	@Test
	public void testItemValueAfterNewLine() {
		doTest(true, true);
	}

	@Test
	public void testEmptyLineBeginning() {
		doTest(true, true);
	}

	@Test
	public void testErrorInlineArray() {
		doTest(true, false);
	}

	@Test
	public void testErrorClosingBracket2() {
		doTest(true, false);
	}

	@Test
	public void testErrorClosingBracket() {
		doTest(true, false);
	}

	@Test
	public void testErrorTabSpaceMixing() {
		doTest(true, true);
	}

	@Test
	public void testErrorIndent() {
		doTest(true, false);
	}

}
