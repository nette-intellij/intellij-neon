package cz.juzna.intellij.neon.psi.impl.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import cz.juzna.intellij.neon.psi.elements.NeonClassReferenceElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public abstract class NeonClassReferenceElementImpl extends NeonPsiElementImpl implements NeonClassReferenceElement {
	public NeonClassReferenceElementImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon class reference";
	}

	@Nullable
	public PsiReference getReference() {
		PsiReference[] references = getReferences();
		return references.length == 0 ? null : references[0];
	}

	@NotNull
	public PsiReference[] getReferences() {
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}
}
