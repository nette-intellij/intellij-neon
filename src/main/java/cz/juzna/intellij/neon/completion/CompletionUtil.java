package cz.juzna.intellij.neon.completion;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.util.PsiTreeUtil;
import cz.juzna.intellij.neon.config.NeonKeyChain;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompletionUtil {

	public static boolean isIncompleteKey(PsiElement el) {
		if (!NeonTokenTypes.STRING_LITERALS.contains(el.getNode().getElementType())) {
			return false;
		}

		//first scalar in file
		if (el.getParent() instanceof NeonScalarValue && el.getParent().getParent() instanceof NeonFile) {
			return true;
		}
		//error element
		if (el.getNode().getElementType() == NeonTokenTypes.NEON_LITERAL && el.getParent() instanceof NeonFile) {
			return true;
		}
		//error element
		if (el.getParent() instanceof NeonArray
			&& el.getPrevSibling() instanceof PsiErrorElement
			&& ((PsiErrorElement) el.getPrevSibling()).getErrorDescription().equals("Expected key-val or array item")) {
			return true;
		}
		//new key after new line
		if (el.getParent() instanceof NeonValue && el.getParent().getParent() instanceof NeonScalarValue
			&& el.getParent().getParent().getParent() instanceof NeonKeyValPair
			&& el.getParent().getParent().getPrevSibling() instanceof NeonIndent
		) {
			return true;
		}

		//last key in file
		NeonKeyValPair keyValuePair = CompletionUtil.findCurrentKeyValuePair(el);
		if (keyValuePair != null && keyValuePair.isLastKey()) {
			return true;
		}

		return false;
	}

	/**
	 * Get full name of property at given element (e.g. common.services.myService1.setup)
	 */
	public static NeonKeyChain getKeyChain(PsiElement el) {
		List<String> names = new ArrayList<>();

		while (el != null) {
			if (el instanceof NeonKeyValPair) {
				if (((NeonKeyValPair) el).getKeyText().length() > 0) {
					names.add(((NeonKeyValPair) el).getKeyText());
				}

				if (el instanceof NeonMainArray) {
					break;
				}

			} else if (el instanceof NeonArrayKeyValuePair) {
				if (((NeonArrayKeyValuePair) el).getKeyText().length() > 0) {
					names.add(((NeonArrayKeyValuePair) el).getKeyText());
				}

			}

			el = el.getParent();
		}
		Collections.reverse(names);
		return NeonKeyChain.get(names.toArray(new String[0]));
	}

	@Nullable
	public static NeonKeyValPair findCurrentKeyValuePair(PsiElement el) {
		if (el instanceof NeonKeyValPair) {
			return (NeonKeyValPair) el;
		}
		return PsiTreeUtil.getParentOfType(el, NeonKeyValPair.class);
	}

}
