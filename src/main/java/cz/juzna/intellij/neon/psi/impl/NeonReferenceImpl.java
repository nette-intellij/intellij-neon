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
		return "Neon reference";
	}

	@Override
	public String getServiceName() {
		return getNode().getText().substring(1);
	}

	@Override
	public String getName() {
		return getNode().getText();
	}
}
