package cz.juzna.intellij.neon.parser;

import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;

/**
 * Types of elements returned from parser
 */
public interface NeonElementTypes {
	public static final IFileElementType FILE = new IFileElementType(NeonLanguage.INSTANCE);

	public static final NeonElementType KEY_VALUE_PAIR = new NeonElementType("Key value pair");
	public static final NeonElementType KEY = new NeonElementType("Key");
	public static final NeonElementType COMPOUND_KEY = new NeonElementType("Compound key");
	public static final NeonElementType HASH = new NeonElementType("Hash");
	public static final NeonElementType ITEM = new NeonElementType("Item");
	public static final NeonElementType ENTITY = new NeonElementType("Entity");
	public static final NeonElementType CHAINED_ENTITY = new NeonElementType("Chained entity");
	public static final NeonElementType STRING = new NeonElementType("String");
	public static final NeonElementType ARRAY = new NeonElementType("Array");
	public static final NeonElementType ARGS = new NeonElementType("Args");
	public static final NeonElementType COMPOUND_VALUE = new NeonElementType("Compound value");
	public static final NeonElementType SCALAR_VALUE = new NeonElementType("Scalar value");
	public static final NeonElementType KEY_USAGE = new NeonElementType("Key usage");
	public static final NeonElementType PARAMETER_USAGE = new NeonElementType("Parameter usage");
	public static final NeonElementType REFERENCE = new NeonElementType("Reference");
}
