package cz.juzna.intellij.neon.parser;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.parser.NeonElementType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;

/**
 * Types of elements returned from parser
 */
public interface NeonElementTypes {
	public static final IFileElementType FILE = new IFileElementType(NeonLanguage.LANGUAGE);

	public static final NeonElementType KEY_VALUE_PAIR = new NeonElementType("Key value pair");
	public static final NeonElementType KEY = new NeonElementType("Key");
	public static final NeonElementType COMPOUND_KEY = new NeonElementType("Compound key");
	public static final NeonElementType HASH = new NeonElementType("Hash");
	public static final NeonElementType ITEM = new NeonElementType("Item");
	public static final NeonElementType ENTITY = new NeonElementType("Entity");
	public static final NeonElementType ARRAY = new NeonElementType("Array");
	public static final NeonElementType ARGS = new NeonElementType("Args");
	public static final NeonElementType SEQUENCE = new NeonElementType("Sequence");
	public static final NeonElementType COMPOUND_VALUE = new NeonElementType("Compound value");
	public static final NeonElementType SCALAR_VALUE = new NeonElementType("Scalar value");

	public static final TokenSet SCALAR_VALUES = TokenSet.create(
		NeonTokenTypes.NEON_STRING,
		NeonTokenTypes.NEON_NUMBER,
		NeonTokenTypes.NEON_REFERENCE,
		NeonTokenTypes.NEON_IDENTIFIER,
		NeonTokenTypes.NEON_LITERAL,
		NeonTokenTypes.NEON_VARIABLE
	);
}
