package cz.juzna.intellij.neon.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

/**
 * Section is a special type of key-val pair - in first indention level
 */
public interface NeonSection extends NeonKeyValPair, PsiNamedElement {
	// for section inheritance
	public PsiElement getParentSection();
	public String getParentSectionText();
}
