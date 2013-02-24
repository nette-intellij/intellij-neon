package cz.juzna.intellij.neon.lexer;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NeonTokenizer {

//	/** @var array */
//	private static $patterns = array(
//		'
//			\'[^\'\n]*\' |
//			"(?: \\\\. | [^"\\\\\n] )*"
//		', // string
//		'
//			-(?= \s | $ ) |
//			:(?= [\s,\]})] | $ ) |
//			[,=[\]{}()]
//		', // symbol
//		'?:\#.*', // comment
//		'\n[\t\ ]*', // new line + indent
//		'
//			[^#"\',=[\]{}()\x00-\x20!`]
//			(?:
//				[^,:=\]})(\x00-\x20]+ |
//				:(?! [\s,\]})] | $ ) |
//				[\ \t]+ [^#,:=\]})(\x00-\x20]
//			)*
//		', // literal / boolean / integer / float
//		'?:[\t\ ]+', // whitespace
//	);
//
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

		

		return tokens;
	}
}