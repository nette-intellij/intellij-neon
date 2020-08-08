package cz.juzna.intellij.neon.lexer;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NeonTokenizer {

	private static String[] patterns = new String[] {
		"\'[^\'\n]*\'|\"(?:\\\\.|[^\"\\\\\n])*\" ", // string
		"-(?=\\s|$)|:(?=[\\s,\\]})]|$)|[,=\\[\\]{}\\(\\)]", // symbol
		"#.*", // comment
		"\n[\t ]*", // new line + indent
		"[^#\"\',=\\[\\]{}\\(\\)\\x00-\\x20!`](?:[^,:=\\]}\\)\\(\\x00-\\x20]+|:(?![\\s,\\]})]|$)|[ \t]+[^#,:=\\]}\\)\\(\\x00-\\x20])*", // literal / boolean / integer / float
		"[\t ]+" // whitespace
	};


	public static ArrayList<Pair<IElementType,String>> parse(@NotNull String string) {
		ArrayList<Pair<IElementType,String>> tokens = tokenize(string);



		return tokens;
	}

	private static ArrayList<Pair<IElementType,String>> tokenize(@NotNull String string) {
		ArrayList<Pair<IElementType,String>> tokens = new ArrayList<Pair<IElementType, String>>();

		Pattern p = createPattern();
		Matcher m = p.matcher(string);
		while(m.find()) {
			Pair<IElementType, String> p2 = getToken(m);
			if (p2 != null) tokens.add(p2);
		}

		return tokens;
	}

	public static Pair<IElementType, String> getToken(Matcher m) {
		Pair<IElementType, String> p;

		if (m.group(1) != null) p = new Pair<IElementType, String>(NeonTokenTypes.NEON_STRING, m.group());
		else if (m.group(2) != null) p = new Pair<IElementType, String>(NeonTokenTypes.NEON_SYMBOL, m.group());
		else if (m.group(3) != null) p = new Pair<IElementType, String>(NeonTokenTypes.NEON_COMMENT, m.group());
		else if (m.group(4) != null) p = new Pair<IElementType, String>(NeonTokenTypes.NEON_INDENT, m.group());
		else if (m.group(5) != null) p = new Pair<IElementType, String>(NeonTokenTypes.NEON_LITERAL, m.group());
		else if (m.group(6) != null) p = new Pair<IElementType, String>(NeonTokenTypes.NEON_WHITESPACE, m.group());
		else p = null;

		return p;
	}

	public static Pattern createPattern() {
		return Pattern.compile("(?:(" + implode(")|(", patterns) + "))");
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
