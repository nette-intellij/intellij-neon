package cz.juzna.intellij.neon.lexer.debug;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.yaml.lexer._YAMLLexer;

import java.io.Reader;

public class YamlTest {

	public static void main(String[] args) throws Exception {
		String str = "pepa: novak\nfranta:\n - vopicka\n - kralik\n";
		System.out.println(str);

		FlexLexer lexer = new _YAMLLexer((Reader) null);
		IElementType el;

		lexer.reset(str, 0, str.length(), _YAMLLexer.YYINITIAL);

		while ((el = lexer.advance()) != null) {
			System.out.printf("%s: %d %d '%s'\n", el.toString(), lexer.getTokenStart(), lexer.getTokenEnd(), str.substring(lexer.getTokenStart(), lexer.getTokenEnd()));
		}
	}

}
