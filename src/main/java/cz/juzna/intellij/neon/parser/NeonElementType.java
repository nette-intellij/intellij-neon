package cz.juzna.intellij.neon.parser;

import com.intellij.psi.tree.IElementType;
import cz.juzna.intellij.neon.NeonLanguage;
import org.jetbrains.annotations.NotNull;


public class NeonElementType extends IElementType {
	public NeonElementType(@NotNull String debugName) {
		super(debugName, NeonLanguage.LANGUAGE);
	}

	public String toString() {
		return "[Neon] " + super.toString();
	}
}
