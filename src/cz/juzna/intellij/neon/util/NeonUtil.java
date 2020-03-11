package cz.juzna.intellij.neon.util;

import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

public class NeonUtil {
	public static String normalizeKeyName(String keyName) {
		if (keyName.endsWith(":")) {
			keyName = keyName.substring(0, keyName.length() - 1);
		}
		return keyName;
	}

	public static boolean isStringAtCaret(@NotNull Editor editor, @NotNull String string) {
		int startOffset = editor.getCaretModel().getOffset();
		String fileText = editor.getDocument().getText();
		return fileText.length() >= startOffset + string.length() && fileText.substring(startOffset, startOffset + string.length()).equals(string);
	}

}
