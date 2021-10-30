package cz.juzna.intellij.neon.psi.elements;

import com.intellij.psi.PsiNamedElement;

/**
 * Key-value pair, part of NeonHash
 */
public interface NeonKeyValPair extends PsiNamedElement, NeonPsiElement {
	// key
	public NeonKey getKey();
	public String getKeyText();

	// value
	public NeonValue getValue();
}
