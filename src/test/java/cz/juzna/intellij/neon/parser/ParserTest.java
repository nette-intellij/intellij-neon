package cz.juzna.intellij.neon.parser;

import com.intellij.lang.LanguageASTFactory;
import com.intellij.lang.ParserDefinition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.PlatformTestCase;
import cz.juzna.intellij.neon.NeonLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class ParserTest extends ParsingTestCase {
	public ParserTest() {
		super("", "neon", new NeonParserDefinition());
		PlatformTestCase.initPlatformLangPrefix();
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		// addExplicitExtension(LanguageASTFactory.INSTANCE, NeonLanguage.LANGUAGE, ?? );
	}

	@Override
	protected String getTestDataPath() {
		return "src/test/data";
	}


	public void testSample01() {
		PsiFile file = createPsiFile("01.neon", getTestDataPath() + "/01.neon");
		ensureParsed(file);

		for(PsiElement el : file.getChildren()) {
			System.out.println(el.getText());
		}
	}



//
//	public static void main(String[] args) {
//		new ParserTest().testSample01();
//	}

	/*** helpers ***/

	/**
	 * Make sure a file gets parsed, by walking thru its structure
	 */
	private void ensureParsed(PsiFile file) {
		// Ensure parsed
		file.accept(new PsiElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				element.acceptChildren(this);
			}
		});
	}
}
