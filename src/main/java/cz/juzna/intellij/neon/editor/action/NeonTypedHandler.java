package cz.juzna.intellij.neon.editor.action;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import cz.juzna.intellij.neon.NeonLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NeonTypedHandler extends TypedHandlerDelegate {

	private static Map<Character, Character> pairs = new HashMap<Character, Character>(3);
	private static Set<Character> chars = new HashSet<Character>(3);

	static {
		pairs.put('{', '}');
		pairs.put('(', ')');
		pairs.put('[', ']');
		chars.add('}');
		chars.add(')');
		chars.add(']');
	}

	@NotNull
	@Override
	public TypedHandlerDelegate.Result beforeCharTyped(char charTyped, Project project, Editor editor, PsiFile file, FileType fileType) {
		// ignores typing '}' before '}'
		if (chars.contains(charTyped) && project != null && file.getViewProvider().getLanguages().contains(NeonLanguage.INSTANCE)) {
			CaretModel caretModel = editor.getCaretModel();
			String text = editor.getDocument().getText();
			int offset = caretModel.getOffset();
			if (text.length() > offset && text.charAt(offset) == charTyped) {
				caretModel.moveToOffset(offset + 1);
				return TypedHandlerDelegate.Result.STOP;
			}
		}
		return super.beforeCharTyped(charTyped, project, editor, file, fileType);
	}

	@NotNull
	@Override
	public TypedHandlerDelegate.Result charTyped(char charTyped, Project project, @NotNull Editor editor, @NotNull PsiFile file) {
		// auto-inserts '}' after typing '{'
		if (pairs.containsKey(charTyped) && project != null && file.getViewProvider().getLanguages().contains(NeonLanguage.INSTANCE)) {
			int offset = editor.getCaretModel().getOffset();
			String text = editor.getDocument().getText();
			Character pairChar = pairs.get(charTyped);
			if (text.length() == offset || text.charAt(offset) != pairChar) {
				editor.getDocument().insertString(offset, pairChar.toString());
				return TypedHandlerDelegate.Result.STOP;
			}
		}

		return super.charTyped(charTyped, project, editor, file);
	}
}
