package cz.juzna.intellij.neon.psi;

import com.intellij.psi.PsiNameIdentifierOwner;

/**
 * Scalar value
 */
public interface NeonScalar extends PsiNameIdentifierOwner, NeonValue {
	public String getValueText();
}
