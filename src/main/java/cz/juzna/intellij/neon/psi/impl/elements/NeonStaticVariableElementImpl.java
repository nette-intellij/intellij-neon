package cz.juzna.intellij.neon.psi.impl.elements;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.stubs.IStubElementType;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpStaticVariableStub;
import cz.juzna.intellij.neon.psi.NeonStaticVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NeonStaticVariableElementImpl extends StubBasedPsiElementBase<NeonPhpStaticVariableStub> implements NeonStaticVariable {
	public NeonStaticVariableElementImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public NeonStaticVariableElementImpl(final NeonPhpStaticVariableStub stub, final IStubElementType nodeType) {
		super(stub, nodeType);
	}

	public String toString() {
		return "Neon static variable";
	}

	@Override
	public String getPhpElementName() {
		return getVariableName();
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
