package cz.juzna.intellij.neon.psi.elements;

import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import cz.juzna.intellij.neon.psi.NeonScalarValue;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * Key from key-value pair
 */
public interface NeonArrayElement extends NeonPsiElement {
	@NotNull
	List<NeonKeyValPair> getKeyValPairList();

	HashMap<String, NeonScalarValue> getMap();

	List<NeonKey> getKeys();

}
