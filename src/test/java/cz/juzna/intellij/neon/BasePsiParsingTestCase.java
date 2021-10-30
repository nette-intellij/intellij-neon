package cz.juzna.intellij.neon;

import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.TestDataFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

abstract public class BasePsiParsingTestCase extends ParsingTestCase {

	protected BasePsiParsingTestCase(@NotNull ParserDefinition parserDefinition) {
		super("", "neon", parserDefinition);
	}

	protected String loadFile(@NotNull @NonNls @TestDataFile String name) throws IOException {
		return FileUtil.loadFile(new File(myFullDataPath, name), CharsetToolkit.UTF8, true);
	}

	protected PsiFile parseFile(@NotNull String fileName) throws IOException {
		return parseFile(fileName, loadFile(fileName));
	}
}
