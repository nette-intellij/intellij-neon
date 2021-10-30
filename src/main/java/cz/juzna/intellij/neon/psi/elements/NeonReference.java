package cz.juzna.intellij.neon.psi.elements;

/**
 * Reference to an service: @database
 * TODO: for future version
 */
public interface NeonReference extends NeonValue {
	public String getServiceName();
}
