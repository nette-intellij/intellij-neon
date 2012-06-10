package cz.juzna.intellij.neon.psi;

/**
 * Reference to an service: @database
 */
public interface NeonReference extends NeonValue {
	public String getServiceName();
}
