package cz.juzna.intellij.neon.psi.impl.elements;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiFile;
import cz.juzna.intellij.neon.psi.elements.NeonFile;
import cz.juzna.intellij.neon.psi.elements.NeonPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base of all PsiElements (or unknown/not-recognized elements)
 */
public class NeonPsiElementImpl extends ASTWrapperPsiElement implements NavigatablePsiElement, NeonPsiElement {
	private Project project = null;
	private NeonFile file = null;

	public NeonPsiElementImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public @Nullable NeonFile getNeonFile() {
		if (file == null) {
			PsiFile containingFile = getContainingFile();
			file = containingFile instanceof NeonFile ? (NeonFile) containingFile : null;
		}
		return file;
	}

	@Override
	public @NotNull Project getProject() {
		if (project == null) {
			project = super.getProject();
		}
		return project;
	}
}
