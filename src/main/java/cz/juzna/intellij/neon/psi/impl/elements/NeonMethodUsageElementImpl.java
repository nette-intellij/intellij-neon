package cz.juzna.intellij.neon.psi.impl.elements;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.stubs.IStubElementType;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpMethodStub;
import cz.juzna.intellij.neon.psi.NeonMethodUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NeonMethodUsageElementImpl extends StubBasedPsiElementBase<NeonPhpMethodStub> implements NeonMethodUsage {
	public NeonMethodUsageElementImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public NeonMethodUsageElementImpl(final NeonPhpMethodStub stub, final IStubElementType nodeType) {
		super(stub, nodeType);
	}

	public String toString() {
		return "Neon method usage";
	}

	@Override
	public String getPhpElementName() {
		return getMethodName();
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
