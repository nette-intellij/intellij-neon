package cz.juzna.intellij.neon.lexer.debug;

import com.intellij.psi.tree.IElementType;
import cz.juzna.intellij.neon.lexer._NeonLexer;

import java.io.Reader;

public class Test {

	public static void main(String[] args) throws Exception {
		String str = "common:\n" +
				"\tparameters:\n" +
				"\t\tdays: [ Mon, Tue, Wed ]\n" +
				"\t\tdays2:\n" +
				"\t\t\t- Mon # Monday\n" +
				"\t\t\t- Tue\n" +
				"\t\t\t- Wed\n" +
				"\n" +
				"\t\t# Third type\n" +
				"\t\tdayNames: { mon: Monday, tue: Tuesday }\n" +
				"\n" +
				"\t\taddress:\n" +
				"\t\t\tstreet: 742 Evergreen Terrace\n" +
				"\t\t\tcity: Springfield\n" +
				"\t\t\tcountry: USA\n" +
				"\n" +
				"\t\tphones: { home: 555-6528, work: 555-7334 }\n" +
				"\n" +
				"\t\tchildren:\n" +
				"\t\t\t- Bart\n" +
				"\t\t\t- Lisa\n" +
				"\t\t\t- %children.third%\n" +
				"\n" +
				"\t\tentity: Column(type=\"integer\")\n" +
				"\n" +
				"\tservices:\n" +
				"\t\tmyService: Nette\\Object(\"AHOJ\")\n" +
				"\t\tmyService2: Nette\\Something(@myService, 1)\n" +
				"\n" +
				"\n" +
				"production < common:\n" +
				"\tservices:\n" +
				"\t\tauthenticator: Nette\\Authenticator(@db)\n" +
				"\n" +
				"development < common:\n";
		System.out.println(str);

		_NeonLexer lexer = new _NeonLexer((Reader) null);
		IElementType el;

		lexer.reset(str, 0, str.length(), _NeonLexer.YYINITIAL);

		while ((el = lexer.advance()) != null) {
			System.out.printf("%s: %d %d '%s'\n", el.toString(), lexer.getTokenStart(), lexer.getTokenEnd(), str.substring(lexer.getTokenStart(), lexer.getTokenEnd()));
		}
	}

}
