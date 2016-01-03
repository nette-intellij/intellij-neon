package cz.juzna.intellij.neon.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.NavigatablePsiElement;
import cz.juzna.intellij.neon.psi.NeonPsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Base of all PsiElements (or unknown/not-recognized elements)
 */
public class NeonPsiElementImpl extends ASTWrapperPsiElement implements NavigatablePsiElement, NeonPsiElement {
	public NeonPsiElementImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	// some common logic should come here
}
