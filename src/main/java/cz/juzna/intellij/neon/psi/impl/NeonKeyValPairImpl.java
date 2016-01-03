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
 		ASTNode keys[];

		ASTNode[] compoundKeys = getNode().getChildren(TokenSet.create(NeonElementTypes.COMPOUND_KEY));
		if (compoundKeys.length > 0) {
			keys = compoundKeys[0].getChildren(TokenSet.create(NeonElementTypes.KEY));
		} else {
			keys = getNode().getChildren(TokenSet.create(NeonElementTypes.KEY));
		}

		return keys.length > 0 ? (NeonKey) keys[0].getPsi() : null;
	}

	@Override
	public String getKeyText() {
		return this.getKey() != null ? this.getKey().getText() : null;
	}

	@Override
	public NeonValue getValue() {
		if (getLastChild() instanceof NeonValue) {
			return (NeonValue) getLastChild();
		}
		return null;
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException {
		// TODO: needed for refactoring
		return null;
	}

	@Override
	public String getName() {
		return getKeyText();
	}
}
