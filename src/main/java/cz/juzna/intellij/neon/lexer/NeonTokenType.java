package cz.juzna.intellij.neon.lexer;

import com.intellij.psi.tree.IElementType;
import cz.juzna.intellij.neon.NeonLanguage;
import org.jetbrains.annotations.NotNull;


public class NeonTokenType extends IElementType {
	public NeonTokenType(@NotNull String debugName) {
		super(debugName, NeonLanguage.INSTANCE);
	}

	public String toString() {
		return "[Neon] " + super.toString();
	}
}
