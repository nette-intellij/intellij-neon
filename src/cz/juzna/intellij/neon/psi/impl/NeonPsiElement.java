package cz.juzna.intellij.neon.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * Base of all PsiElements
 */
public class NeonPsiElement extends ASTWrapperPsiElement {
	public NeonPsiElement(@NotNull ASTNode astNode) {
		super(astNode);
	}

	// some common logic should come here
}
