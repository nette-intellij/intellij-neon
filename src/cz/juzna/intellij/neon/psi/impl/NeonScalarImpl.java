package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonScalar;
import cz.juzna.intellij.neon.reference.NeonElementFactory;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class NeonScalarImpl extends NeonPsiElementImpl implements NeonScalar {
	public NeonScalarImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon scalar";
	}

	@Override
	public String getValueText() {
		PsiElement firstChild = getFirstChild();
		if (firstChild == null) {
			return getText();
		}

		String text = getFirstChild().getText();
		if (getFirstChild() instanceof LeafPsiElement && ((LeafPsiElement) getFirstChild()).getElementType() == NeonTokenTypes.NEON_STRING) {
			text = text.substring(1, text.length() - 1);
		}
		return text;
	}

	@Override
	public @Nullable PsiElement getNameIdentifier() {
		return null;
	}

	@Override
	public PsiElement setName(@NotNull String newName) throws IncorrectOperationException {
		ASTNode keyNode = this.getFirstChild().getNode();
		String namespace = this.getText().substring(0, this.getText().lastIndexOf("\\"));
		PsiElement classType = NeonElementFactory.createClassType(this.getProject(), namespace + "\\" + newName);
		if (classType == null) {
			return this;
		}
		return NeonPhpUtil.replaceChildNode(this, classType, keyNode);
	}

	public boolean isPhpScalar() {
		return NeonPhpUtil.isPhpClassScalar(this);
	}

	public boolean isInString() {
		return NeonPhpUtil.isPhpClassScalar(this);
	}

	@Override
	public String getName() {
		return getValueText();
	}

	@Override
	public String getNormalizedClassName() {
		return NeonPhpUtil.normalizeClassName(getValueText());
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
