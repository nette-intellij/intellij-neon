package cz.juzna.intellij.neon.psi;

import com.intellij.psi.PsiFile;
import cz.juzna.intellij.neon.psi.elements.NeonPsiElement;

/**
 * Neon file - just a wrapper for the whole file
 */
public interface NeonFile extends PsiFile, NeonPsiElement {


	/**
	 * returns root value
	 */
	public NeonPsiElement getValue();

}
