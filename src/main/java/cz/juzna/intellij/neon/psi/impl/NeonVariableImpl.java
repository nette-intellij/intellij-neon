package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import cz.juzna.intellij.neon.psi.NeonVariable;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NeonVariableImpl extends NeonPsiElementImpl implements NeonVariable {
	public NeonVariableImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	@Override
	public String getVariableText() {
		return getNode().getText();
	}
}
