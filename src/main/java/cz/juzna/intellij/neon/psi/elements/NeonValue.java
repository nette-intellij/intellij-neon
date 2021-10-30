package cz.juzna.intellij.neon.psi.elements;

import com.intellij.psi.PsiElement;

/**
 * Parent for other values - can be Scalar or a compound value - array, entity, ...
 */
public interface NeonValue extends PsiElement, NeonPsiElement {

}
