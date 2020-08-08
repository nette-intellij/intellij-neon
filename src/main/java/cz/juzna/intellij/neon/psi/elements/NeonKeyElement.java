package cz.juzna.intellij.neon.psi.elements;

import com.intellij.psi.PsiNameIdentifierOwner;
import cz.juzna.intellij.neon.config.NeonKeyChain;
import cz.juzna.intellij.neon.psi.elements.NeonPsiElement;

/**
 * Key from key-value pair
 */
public interface NeonKeyElement extends NeonPsiElement, PsiNameIdentifierOwner {
	public String getKeyText();

	public NeonKeyChain getKeyChain(boolean isIncomplete);

	public boolean isServiceDefinition();

	public boolean isParameterDefinition();

}
