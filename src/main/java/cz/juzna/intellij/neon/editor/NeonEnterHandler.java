package cz.juzna.intellij.neon.editor;

import com.intellij.application.options.CodeStyle;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.util.PsiUtilCore;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.parser.NeonElementTypes;
import cz.juzna.intellij.neon.psi.NeonArray;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class NeonEnterHandler implements EnterHandlerDelegate {


	@Override
	public Result preprocessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull Ref<Integer> caretOffset, @NotNull Ref<Integer> caretAdvance, @NotNull DataContext dataContext, @Nullable EditorActionHandler originalHandler) {
		return Result.Continue;
	}

	@Override
	public Result postProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext) {
		if (!(file instanceof NeonFile)) {
			return null;
		}
		int offset = editor.getCaretModel().getOffset();
		Document document = editor.getDocument();
		PsiElement el = PsiUtilCore.getElementAtOffset(file, offset - 1);
		PsiElement prev1 = PsiUtilCore.getElementAtOffset(file, offset - el.getText().length() - 1);
		PsiElement prev2 = PsiUtilCore.getElementAtOffset(file, offset - el.getText().length() - 2);
		if (prev1.getText().equals("\"") && prev2.getText().equals("\"\"")) {
			document.insertString(offset, "\n" + el.getText().substring(1) + "\"\"\"");
			return null;
		} else if (prev1.getText().equals("'") && prev2.getText().equals("''")) {
			document.insertString(offset, "\n" + el.getText().substring(1) + "'''");
			return null;
		}
		if (!shouldProcess(el)) {
			return null;
		}
		String indent = getIndentString(file, isKeyAfterBullet(el.getParent()));
		document.insertString(offset, indent);
		editor.getCaretModel().moveToOffset(offset + indent.length());
		EditorModificationUtil.scrollToCaret(editor);
		editor.getSelectionModel().removeSelection();
		return null;
	}

	private boolean shouldProcess(PsiElement el) {
		PsiElement prev = el.getPrevSibling();
		if (prev != null && prev.getNode().getElementType() == NeonTokenTypes.NEON_ITEM_DELIMITER) { //inline array
			return false;
		}
		if (prev != null && prev.getNode().getElementType() == NeonTokenTypes.NEON_INDENT) { //empty line
			return false;
		}
		PsiElement parent = el.getParent();
		if (parent == null) {
			return false;
		}
		return parent instanceof NeonKeyValPair || parent.getNode().getElementType() == NeonElementTypes.ITEM || isKeyAfterBullet(parent);
	}

	@NotNull
	private String getIndentString(@NotNull PsiFile file, boolean keyAfterBullet) {
		if (keyAfterBullet) {
			return "  ";
		}

		CommonCodeStyleSettings settings = CodeStyle.getLanguageSettings(file, NeonLanguage.INSTANCE);
		CommonCodeStyleSettings.IndentOptions indentOpt = settings.getIndentOptions();
		return indentOpt == null || indentOpt.USE_TAB_CHARACTER ? "\t" : StringUtil.repeat(" ", indentOpt.INDENT_SIZE);
	}

	private boolean isKeyAfterBullet(PsiElement el) {
		el = el instanceof NeonKeyValPair ? el.getParent() : el;

		return el instanceof NeonArray
				&& el.getChildren().length == 1
				&& el.getParent().getNode().getElementType() == NeonElementTypes.ITEM
				&& el.getPrevSibling().getText().equals(" ")
				&& el.getParent().getFirstChild().getText().equals("-");
	}

}
