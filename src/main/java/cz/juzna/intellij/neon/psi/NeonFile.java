package cz.juzna.intellij.neon.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiFile;

import java.util.HashMap;

/**
 * Neon file
 */
public interface NeonFile extends PsiFile, NavigatablePsiElement {
	public HashMap<String, NeonSection> getSections();
}
