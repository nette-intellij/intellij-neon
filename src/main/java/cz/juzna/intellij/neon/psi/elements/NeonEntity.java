package cz.juzna.intellij.neon.psi.elements;

import com.intellij.psi.PsiNamedElement;

/**
 * Entity - identifier with arguments
 */
public interface NeonEntity extends NeonValue, PsiNamedElement {
	public NeonArray getArgs();
}
