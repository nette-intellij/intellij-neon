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
		ArrayList<Pair<IElementType,String>> tokens = new ArrayList<Pair<IElementType, String>>();

		Pattern p = Pattern.compile("(?:(" + implode(")|(", patterns) + "))");
		Matcher m = p.matcher(string);
		while(m.find()) {
			if (m.group(1) != null) tokens.add(new Pair<IElementType, String>(NeonTokenTypes.NEON_STRING, m.group()));
			else if(m.group(2) != null) tokens.add(new Pair<IElementType, String>(NeonTokenTypes.NEON_SYMBOL, m.group()));
			else if(m.group(3) != null) tokens.add(new Pair<IElementType, String>(NeonTokenTypes.NEON_COMMENT, m.group()));
			else if(m.group(4) != null) tokens.add(new Pair<IElementType, String>(NeonTokenTypes.NEON_INDENT, m.group()));
			else if(m.group(5) != null) tokens.add(new Pair<IElementType, String>(NeonTokenTypes.NEON_LITERAL, m.group()));
			else if(m.group(6) != null) tokens.add(new Pair<IElementType, String>(NeonTokenTypes.NEON_WHITESPACE, m.group()));
			else { /* have fun */ }
		}

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
