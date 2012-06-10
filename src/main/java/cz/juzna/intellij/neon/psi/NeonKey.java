package cz.juzna.intellij.neon.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

/**
 * Key from key-value pair
 */
public interface NeonKey extends NeonPsiElement, PsiNamedElement {
	public PsiElement getKey();
	public String getKeyText();

}
