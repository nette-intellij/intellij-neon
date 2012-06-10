package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;
import cz.juzna.intellij.neon.parser.NeonElementTypes;
import cz.juzna.intellij.neon.psi.NeonEntity;
import cz.juzna.intellij.neon.psi.NeonHash;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NeonEntityImpl extends NeonPsiElementImpl implements NeonEntity {
	public NeonEntityImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon entity";
	}

	@Override
	public String getName() {
		return getNode().getFirstChildNode().getPsi().getText();
	}

	@Override
	public NeonHash getArgs() {
		ASTNode children[] = getNode().getChildren(TokenSet.create(NeonElementTypes.ARGS));
		if (children.length > 0) return (NeonHash) children[0].getPsi(); // should be hash I guess
		else return null;
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException {
		return null; // TODO:
	}
}
