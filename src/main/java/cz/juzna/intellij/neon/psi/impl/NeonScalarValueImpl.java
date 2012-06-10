package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import cz.juzna.intellij.neon.psi.NeonScalarValue;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NeonScalarValueImpl extends NeonPsiElementImpl implements NeonScalarValue {
	public NeonScalarValueImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "neon scalar value";
	}

	@Override
	public String getValueText() {
		return getNode().getText();
	}
}
