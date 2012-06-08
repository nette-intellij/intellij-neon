package cz.juzna.intellij.neon;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.lexer.NeonElementType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;

/**
 * Types of elements returned from parser
 */
public interface NeonElementTypes {
	public static final IFileElementType FILE = new IFileElementType(NeonLanguage.LANGUAGE);

	public static final NeonElementType KEY_VALUE_PAIR = new NeonElementType("Key value pair");
	public static final NeonElementType HASH = new NeonElementType("Hash");
	public static final NeonElementType ARRAY = new NeonElementType("Array");
	public static final NeonElementType SEQUENCE = new NeonElementType("Sequence");
	public static final NeonElementType COMPOUND_VALUE = new NeonElementType("Compound value");
	public static final NeonElementType SCALAR_LIST_VALUE = new NeonElementType("Scalar list value");
	public static final NeonElementType SCALAR_TEXT_VALUE = new NeonElementType("Scalar text value");

	public static final TokenSet SCALAR_VALUES = TokenSet.create(new IElementType[] {
		// TODO: add more
		NeonTokenTypes.NEON_STRING,
		NeonTokenTypes.NEON_NUMBER,
		SCALAR_LIST_VALUE
	});
}
