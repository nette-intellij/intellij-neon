package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NeonSequenceImpl extends NeonArrayImpl {
	public NeonSequenceImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon sequence";
	}
}
