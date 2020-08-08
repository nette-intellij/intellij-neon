package cz.juzna.intellij.neon.completion.insert;


import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiDocumentManager;
import com.jetbrains.php.lang.psi.elements.PhpClass;


/**
 * Adds full namespace when a class name is entered by code completion
 */
public class PhpReferenceInsertHandler implements InsertHandler<LookupElement> {
	private static final PhpReferenceInsertHandler[] instances = {
		new PhpReferenceInsertHandler(false), new PhpReferenceInsertHandler(true)
	};

	private boolean incompleteKey;

	public PhpReferenceInsertHandler(boolean incompleteKey) {
		this.incompleteKey = incompleteKey;
	}

	public void handleInsert(InsertionContext context, LookupElement lookupElement) {
		final Object object = lookupElement.getObject();
		final String classNamespace;

		if (object instanceof PhpClass) {
			classNamespace = ((PhpClass) object).getNamespaceName();

		} else {
			classNamespace = "";
		}

		if (!classNamespace.isEmpty()) {
			int startOffset = context.getEditor().getCaretModel().getOffset();
			String fileText = context.getEditor().getDocument().getText();
			String current = fileText.substring(0, startOffset);
			int lastSpace = current.lastIndexOf(" ");
			current = current.substring(lastSpace + 1);
			int index = current.lastIndexOf("\\");
			String existingNamespace = "";
			if (index > 0 && current.length() >= index) {
				existingNamespace = current.substring(0, index) + "\\";
			}

			String fqn = classNamespace;
			if (!existingNamespace.startsWith("\\") && fqn.startsWith("\\")) {
				fqn = fqn.substring(1);
			}

			if (fqn.contains(existingNamespace)) {
				fqn = fqn.replace(existingNamespace, "");
			}

			if (incompleteKey) {
				context.getDocument().insertString(context.getTailOffset(), ": ");
				context.getEditor().getCaretModel().moveToOffset(context.getEditor().getCaretModel().getOffset() + 2);
			}
			context.getDocument().insertString(context.getStartOffset(), fqn);
			PsiDocumentManager.getInstance(context.getProject()).commitDocument(context.getDocument());
		}
	}

	public static PhpReferenceInsertHandler getInstance(boolean incompleteKey) {
		return instances[incompleteKey ? 1 : 0];
	}

}
