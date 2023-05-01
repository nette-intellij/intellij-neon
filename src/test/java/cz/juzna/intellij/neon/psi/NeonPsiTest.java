package cz.juzna.intellij.neon.psi;

import com.intellij.testFramework.HeavyPlatformTestCase;
import cz.juzna.intellij.neon.BasePsiParsingTestCase;
import cz.juzna.intellij.neon.parser.NeonParserDefinition;
import org.junit.Test;

import java.net.URL;


public class NeonPsiTest extends BasePsiParsingTestCase {

	public NeonPsiTest() {
		super(new NeonParserDefinition());
		HeavyPlatformTestCase.doAutodetectPlatformPrefix();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// initialize configuration with test configuration
		//LatteConfiguration.getInstance(getProject(), getXmlFileData());
		//getProject().registerService(LatteSettings.class);
	}

	@Override
	protected String getTestDataPath() {
		URL url = getClass().getClassLoader().getResource("data/psi/neonPsi");
		assert url != null;
		return url.getFile();
	}

	@Test
	public void testDefault() {
		doTest(true, true);
	}

	@Test
	public void testDefault1() {
		doTest(true, true);
	}

	@Test
	public void testDefault2() {
		doTest(true, true);
	}

	@Test
	public void testNested() {
		doTest(true, true);
	}

	@Test
	public void testNested1() {
		doTest(true, true);
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
	public void testArrayAfterKey() {
		doTest(true, true); //todo: fix this eventuality
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
	public void testArrayIndentedFile() {
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
	public void testEmptyLineBeginning() {
		doTest(true, true);
	}

	@Test
	public void testMultiline() {
		doTest(true, true);
	}

	@Test
	public void testItemValueAfterNewLine() {
		doTest(true, true); //todo: fix this eventuality
	}

	@Test
	public void testArrayInline() {
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
	public void testErrorClosingBracket() {
		doTest(true, false);
	}

	@Test
	public void testErrorClosingBracket2() {
		doTest(true, false);
	}

	@Test
	public void testErrorIndent() {
		doTest(true, false);
	}

	@Test
	public void testErrorInlineArray() {
		doTest(true, false);
	}

	@Test
	public void testErrorTabSpaceMixing() {
		doTest(true, false);
	}

}
