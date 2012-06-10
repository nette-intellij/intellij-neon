package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.parser.NeonElementTypes;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import cz.juzna.intellij.neon.psi.NeonValue;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NeonKeyValPairImpl extends NeonPsiElementImpl implements NeonKeyValPair {
	public NeonKeyValPairImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon key-val pair";
	}


	@Override
	public NeonKey getKey() {
		return (NeonKey) getNode().getChildren(TokenSet.create(NeonElementTypes.KEY))[0].getPsi();
	}

	@Override
	public String getKeyText() {
		return this.getKey().getText();
	}

	@Override
	public NeonValue getValue() {
		return (NeonValue) getNode().getLastChildNode().getPsi();
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException {
		// TODO
		return null;
	}

	@Override
	public String getName() {
		return getKeyText();
	}
}
