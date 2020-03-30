package cz.juzna.intellij.neon.completion.insert;


import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import cz.juzna.intellij.neon.NeonLanguage;
import org.jetbrains.annotations.NotNull;

public class KeywordInsertHandler implements InsertHandler<LookupElement> {

	private static final KeywordInsertHandler instance = new KeywordInsertHandler();

	public KeywordInsertHandler() {
		super();
	}

	public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement lookupElement) {
		PsiElement element = context.getFile().findElementAt(context.getStartOffset());
		if (element != null && element.getLanguage() == NeonLanguage.INSTANCE) {
			//PsiElement parent = element.getParent();
			//boolean lastError = parent.getLastChild().getNode().getElementType() == TokenType.ERROR_ELEMENT;

			Editor editor = context.getEditor();
			CaretModel caretModel = editor.getCaretModel();
			String text = editor.getDocument().getText();

			int spaceInserted = 0;
			int offset = caretModel.getOffset();

			int lastBraceOffset = text.indexOf(":", offset);
			int endOfLineOffset = text.indexOf("\n", offset);

			if (endOfLineOffset == -1) {
				endOfLineOffset = text.length();
			}

			offset = offset + 1;
			if (lastBraceOffset == -1 || lastBraceOffset > endOfLineOffset) {
				caretModel.moveToOffset(endOfLineOffset + spaceInserted);
				EditorModificationUtil.insertStringAtCaret(editor, ": ");
				offset = offset + 1;
			}

			caretModel.moveToOffset(offset);
			PsiDocumentManager.getInstance(context.getProject()).commitDocument(editor.getDocument());
		}
	}

	public static KeywordInsertHandler getInstance() {
		return instance;
	}
}