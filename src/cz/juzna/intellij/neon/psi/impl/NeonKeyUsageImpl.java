package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import cz.juzna.intellij.neon.psi.*;
import org.jetbrains.annotations.NotNull;

public class NeonKeyUsageImpl extends NeonPsiElementImpl implements NeonKeyUsage {
	public NeonKeyUsageImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon key usage";
	}

	@Override
	public String getKeyText() {
		return getNode().getText().trim().substring(1);
	}

	@Override
	public String getName() {
		return getKeyText();
	}
}
