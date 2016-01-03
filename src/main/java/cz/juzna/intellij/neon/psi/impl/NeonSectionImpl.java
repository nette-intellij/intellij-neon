package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import cz.juzna.intellij.neon.parser.NeonElementTypes;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonSection;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NeonSectionImpl extends NeonKeyValPairImpl implements NeonSection {
	public NeonSectionImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon section";
	}

	@Override
	public NeonKey getParentSection() {
		if (getNode().getFirstChildNode().getElementType() == NeonElementTypes.COMPOUND_KEY) {
			return (NeonKey) getNode().getPsi().getFirstChild();
		} else {
			return null;
		}
	}

	@Override
	public String getParentSectionText() {
		PsiElement tmp = getParentSection();
		return tmp == null ? null : tmp.getText();
	}
}
