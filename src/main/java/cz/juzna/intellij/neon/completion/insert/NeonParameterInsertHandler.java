package cz.juzna.intellij.neon.completion.insert;


import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiDocumentManager;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.util.NeonUtil;


/**
 * Adds full namespace when a parameter name is entered by code completion
 */
public class NeonParameterInsertHandler implements InsertHandler<LookupElement> {
	private static final NeonParameterInsertHandler instance = new NeonParameterInsertHandler();

	public void handleInsert(InsertionContext context, LookupElement lookupElement) {
		final Object object = lookupElement.getObject();
		final String parameterName;

		if (object instanceof NeonKey) {
			parameterName = ((NeonKey) object).getKeyChain(false).withoutParentKey().toDottedString();

		} else {
			parameterName = "";
		}

		int resultOffset = context.getEditor().getCaretModel().getOffset();
		if (!parameterName.isEmpty()) {
			context.getDocument().insertString(context.getStartOffset(), parameterName.endsWith(".") ? parameterName : (parameterName + "."));
			resultOffset = context.getEditor().getCaretModel().getOffset();
		}

		if (!NeonUtil.isStringAtCaret(context.getEditor(), "%")) {
			context.getDocument().insertString(context.getTailOffset(), "%");
			resultOffset = context.getEditor().getCaretModel().getOffset() + 1;
		}

		int checkedOffset = context.getStartOffset();
		context.getEditor().getCaretModel().moveToOffset(checkedOffset);
		if (!NeonUtil.isStringAtCaret(context.getEditor(), "%")) {
			context.getEditor().getCaretModel().moveToOffset(checkedOffset - 1);
			if (!NeonUtil.isStringAtCaret(context.getEditor(), "%")) {
				context.getDocument().insertString(checkedOffset, "%");
				resultOffset = resultOffset + 1;
			}
		}

		context.getEditor().getCaretModel().moveToOffset(resultOffset);
		PsiDocumentManager.getInstance(context.getProject()).commitDocument(context.getDocument());
	}

	public static NeonParameterInsertHandler getInstance() {
		return instance;
	}

}
