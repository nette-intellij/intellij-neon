package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import cz.juzna.intellij.neon.psi.NeonParameterUsage;
import org.jetbrains.annotations.NotNull;

public class NeonParameterUsageImpl extends NeonPsiElementImpl implements NeonParameterUsage {
	public NeonParameterUsageImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon parameter usage";
	}

	@Override
	public String getKeyText() {
		String s = getNode().getText().trim();
		return s.substring(1, s.length() - 1);
	}

	@Override
	public String getName() {
		return getKeyText();
	}
}
