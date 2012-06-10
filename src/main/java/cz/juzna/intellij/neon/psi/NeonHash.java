package cz.juzna.intellij.neon.psi;

import java.util.HashMap;
import java.util.List;

/**
 * Special type of value
 */
public interface NeonHash extends NeonValue {
	/**
	 * Get keys as nodes
	 */
	public List<NeonKey> getKeys();



	/**
	 * Get all subitems
	 */
	public List<NeonKeyValPair> getItems();



	/**
	 * Get all values as a hash-map
	 */
	public HashMap<String, NeonValue> getMap();
}
