package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import cz.juzna.intellij.neon.psi.NeonReference;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NeonReferenceImpl extends NeonPsiElementImpl implements NeonReference {
	public NeonReferenceImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "neon reference";
	}

	@Override
	public String getReferenceText() {
		return getNode().getText();
	}
}
