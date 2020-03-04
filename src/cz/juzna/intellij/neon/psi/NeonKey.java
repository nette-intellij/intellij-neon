package cz.juzna.intellij.neon.psi;

import cz.juzna.intellij.neon.config.NeonKeyChain;
import org.jetbrains.annotations.Nullable;

/**
 * Key from key-value pair
 */
public interface NeonKey extends NeonPsiElement {
	public String getKeyText();

	public NeonKeyChain getKeyChain(boolean isIncomplete);

	public boolean isServiceDefinition();

	public boolean isParameterDefinition();

	@Nullable
	public NeonKeyValPair getParentKeyValuePair();

	public boolean isMainKey();

}
