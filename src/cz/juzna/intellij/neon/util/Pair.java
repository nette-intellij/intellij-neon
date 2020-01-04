package cz.juzna.intellij.neon.util;

import com.intellij.psi.tree.IElementType;

public class Pair<T, E> extends com.intellij.openapi.util.Pair<T, E> {
	public Pair(T first, E second) {
		super(first, second);
	}

	public static Pair<IElementType, String> of(IElementType first, String second) {
		return new Pair<IElementType, String>(first, second);
	}
}
