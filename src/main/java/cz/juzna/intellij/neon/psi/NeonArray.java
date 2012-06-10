package cz.juzna.intellij.neon.psi;

import java.util.List;

/**
 * Special type of value
 */
public interface NeonArray extends NeonValue {
	/**
	 * Get all subitems
	 */
	public List<NeonValue> getItems();

}
