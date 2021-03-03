package cz.juzna.intellij.neon.psi.impl.elements;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.stubs.IStubElementType;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpConstantStub;
import cz.juzna.intellij.neon.psi.NeonConstantUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NeonConstantUsageElementImpl extends StubBasedPsiElementBase<NeonPhpConstantStub> implements NeonConstantUsage {
	public NeonConstantUsageElementImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public NeonConstantUsageElementImpl(final NeonPhpConstantStub stub, final IStubElementType nodeType) {
		super(stub, nodeType);
	}

	public String toString() {
		return "Neon constant usage";
	}

	@Override
	public String getPhpElementName() {
		return getConstantName();
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
