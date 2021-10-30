package cz.juzna.intellij.neon.psi.elements;

import com.intellij.psi.NavigatablePsiElement;
import org.jetbrains.annotations.Nullable;

/**
 * parent for all elements, or those which don't need a special class
 */
public interface NeonPsiElement extends NavigatablePsiElement {
//	public abstract List<NeonPsiElement> getNeonChildren();
    @Nullable NeonFile getNeonFile();

}
