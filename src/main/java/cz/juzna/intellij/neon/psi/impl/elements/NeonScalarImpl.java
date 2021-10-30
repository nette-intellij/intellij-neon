package cz.juzna.intellij.neon.psi.impl.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.elements.NeonScalar;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NeonScalarImpl extends NeonPsiElementImpl implements NeonScalar {
	public NeonScalarImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon scalar";
	}

	@Override
	public String getValueText() {
		String text = getFirstChild().getText();
		if (getFirstChild() instanceof LeafPsiElement && ((LeafPsiElement) getFirstChild()).getElementType() == NeonTokenTypes.NEON_STRING) {
			text = text.substring(1, text.length() - 1);
		}
		return text;
	}

	@Override
	public String getName() {
		return getValueText();
	}
}
