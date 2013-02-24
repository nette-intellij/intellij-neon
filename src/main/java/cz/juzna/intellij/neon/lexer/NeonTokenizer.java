package cz.juzna.intellij.neon.lexer;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NeonTokenizer {

	private static String[] patterns = new String[] {
		"\'[^\'\n]*\'|\"(?:\\\\.|[^\"\\\\\n])*\" ", // string
		"-(?=\\s|$)|:(?=[\\s,\\]})]|$)|[,=\\[\\]{}\\(\\)]", // symbol
		"?:#.*", // comment
		"\n[\t ]*", // new line + indent
		"[^#\"\',=\\[\\]{}\\(\\)\\x00-\\x20!`](?:[^,:=\\]}\\)\\(\\x00-\\x20]+|:(?![\\s,\\]})]|$)|[ \t]+[^#,:=\\]}\\)\\(\\x00-\\x20])*", // literal / boolean / integer / float
		"?:[\t ]+" // whitespace
	};

//	/** @var Tokenizer */
//	private static $tokenizer;
//
//	private static $brackets = array(
//		'[' => ']',
//		'{' => '}',
//		'(' => ')',
//	);
//
//	/** @var int */
//	private $n = 0;
//
//	/** @var bool */
//	private $indentTabs;

	public static ArrayList<IElementType> parse(@NotNull String string) {
		ArrayList<IElementType> tokens = new ArrayList<IElementType>();

		Pattern p = Pattern.compile("((" + implode(")|(", patterns) + "))");
		Matcher m = p.matcher(string);
		List<String> matches = new ArrayList<String>();
		while(m.find()) matches.add(m.group());

		System.out.println(matches);

		return tokens;
	}

	public static String implode(String separator, String... data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length - 1; i++) {
			//data.length - 1 => to not add separator at the end
			if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
				sb.append(data[i]);
				sb.append(separator);
			}
		}
		sb.append(data[data.length - 1]);
		return sb.toString();
	}
}