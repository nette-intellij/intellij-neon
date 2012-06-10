package cz.juzna.intellij.neon.psi;

import com.intellij.psi.NavigatablePsiElement;

import java.util.List;

/**
 * 
 */
public interface NeonPsiElement extends NavigatablePsiElement {
	public abstract List<NeonPsiElement> getNeonChildren();
}
