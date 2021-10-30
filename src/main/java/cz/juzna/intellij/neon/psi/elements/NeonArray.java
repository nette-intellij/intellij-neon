package cz.juzna.intellij.neon.psi.elements;

import java.util.HashMap;
import java.util.List;

/**
 * php-like array
 *
 * it can be a list, hash-map or a mix of those two
 */
public interface NeonArray extends NeonValue {
	/**
	 * Is it a list-like array? i.e. keys are only numeric
	 */
	public boolean isList();

	/**
	 * Is it hash-map-like array? I.e. keys are not-numeric
	 */
	public boolean isHashMap();

	/**
	 * Get all item values (ignore keys)
	 */
	public List<NeonValue> getValues();

	/**
	 * Get keys as nodes
	 */
	public List<NeonKey> getKeys();

	/**
	 * Get all values as a hash-map
	 */
	public HashMap<String, NeonValue> getMap();

}
