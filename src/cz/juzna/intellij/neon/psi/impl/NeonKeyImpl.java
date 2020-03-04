package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import cz.juzna.intellij.neon.completion.CompletionUtil;
import cz.juzna.intellij.neon.config.NeonKeyChain;
import cz.juzna.intellij.neon.psi.NeonArray;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class NeonKeyImpl extends NeonPsiElementImpl implements NeonKey {
	public NeonKeyImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon key";
	}

	@Override
	public String getKeyText() {
		return getNode().getText().trim();
	}

	@Override
	public boolean isServiceDefinition() {
		return this.getKeyChain(false).equalsMainKey("services");
	}

	@Override
	public boolean isParameterDefinition() {
		return this.getKeyChain(false).equalsMainKey("parameters");
	}

	@Nullable
	@Override
	public NeonKeyValPair getParentKeyValuePair() {
		NeonArray currentArray = PsiTreeUtil.getParentOfType(this, NeonArray.class);
		if (currentArray == null) {
			return null;
		}
		return PsiTreeUtil.getParentOfType(currentArray, NeonKeyValPair.class);
	}

	@Override
	public boolean isMainKey() {
		return getParentKeyValuePair() == null;
	}

	public NeonKeyChain getKeyChain(boolean isIncomplete) {
		return CompletionUtil.getKeyChain(resolveKeyElementForChain(isIncomplete));
	}

	private PsiElement resolveKeyElementForChain(boolean isIncomplete)
	{
		if (isIncomplete) {
			return this.getParent();
		} else if (this.getParent().getParent() instanceof NeonFile) {
			return this.getParent();
		} else  {
			// literal -> scalar -> [key ->] key-val pair -> any parent
			PsiElement el = this.getParent().getParent();
			return el instanceof NeonKey ? el.getParent().getParent() : el.getParent();
		}
	}

	@Override
	public String getName() {
		return getKeyText();
	}
}
