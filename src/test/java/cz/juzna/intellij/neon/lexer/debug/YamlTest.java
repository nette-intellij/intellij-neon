package cz.juzna.intellij.neon.lexer.debug;

import com.intellij.lexer.FlexLexer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.yaml.lexer._YAMLLexer;

import java.io.File;
import java.io.Reader;

public class YamlTest {

	public static void main(String[] args) throws Exception {
		// Load file from command line
		String code = FileUtil.loadFile(new File(args[0]), CharsetToolkit.UTF8).trim();
		code = StringUtil.convertLineSeparators(code);

		FlexLexer lexer = new _YAMLLexer((Reader) null);
		IElementType el;

		lexer.reset(code, 0, code.length(), _YAMLLexer.YYINITIAL);

		while ((el = lexer.advance()) != null) {
			System.out.printf("%s: %d %d '%s'\n", el.toString(), lexer.getTokenStart(), lexer.getTokenEnd(), code.substring(lexer.getTokenStart(), lexer.getTokenEnd()));
		}
	}

}
