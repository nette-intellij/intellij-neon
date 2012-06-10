package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.parser.NeonElementTypes;
import cz.juzna.intellij.neon.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class NeonArrayImpl extends NeonPsiElementImpl implements NeonArray {
	public NeonArrayImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon array";
	}

	@Override
	public List<NeonValue> getItems() {
		ArrayList<NeonValue> result = new ArrayList<NeonValue>();

		for (ASTNode node : getNode().getChildren(null)) {
			PsiElement psi = node.getPsi();
			if (psi instanceof NeonValue) result.add((NeonValue) psi);
		}

		return result;
	}

}
