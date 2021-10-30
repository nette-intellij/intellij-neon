package cz.juzna.intellij.neon.psi.elements;

import com.intellij.psi.PsiFile;

/**
 * Neon file - just a wrapper for the whole file
 */
public interface NeonFile extends PsiFile, NeonPsiElement {


	/**
	 * returns root value
	 */
	NeonPsiElement getValue();

}
