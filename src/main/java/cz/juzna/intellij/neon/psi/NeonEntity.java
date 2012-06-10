package cz.juzna.intellij.neon.psi;

/**
 * Entity - identifier with arguments
 */
public interface NeonEntity extends NeonValue {
	public String getEntityName();
	public NeonHash getArgs();
}
