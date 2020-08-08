package cz.juzna.intellij.neon.completion.insert;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiDocumentManager;
import cz.juzna.intellij.neon.util.NeonUtil;

/**
 * Adds full namespace when a parameter name is entered by code completion
 */
public class NeonServiceInsertHandler implements InsertHandler<LookupElement> {
	private static final NeonServiceInsertHandler instance = new NeonServiceInsertHandler();

	public void handleInsert(InsertionContext context, LookupElement lookupElement) {
		final Object object = lookupElement.getObject();
		String serviceName = object instanceof String ? (String) object : "";

		int prevOffset = context.getEditor().getCaretModel().getOffset();
		int checkedOffset = prevOffset - serviceName.length();
		boolean added = false;
		context.getEditor().getCaretModel().moveToOffset(checkedOffset);
		if (!NeonUtil.isStringAtCaret(context.getEditor(), "@")) {
			context.getEditor().getCaretModel().moveToOffset(checkedOffset - 1);
			if (!NeonUtil.isStringAtCaret(context.getEditor(), "@")) {
				context.getDocument().insertString(checkedOffset, "@");
				context.getEditor().getCaretModel().moveToOffset(prevOffset + 1);
				added = true;
			}
		}

		if (!added) {
			context.getEditor().getCaretModel().moveToOffset(prevOffset);
		}

		PsiDocumentManager.getInstance(context.getProject()).commitDocument(context.getDocument());
	}

	public static NeonServiceInsertHandler getInstance() {
		return instance;
	}

}
