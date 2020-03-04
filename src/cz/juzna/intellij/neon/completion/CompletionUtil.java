package cz.juzna.intellij.neon.completion;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.util.PsiTreeUtil;
import cz.juzna.intellij.neon.config.NeonKeyChain;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.parser.NeonElementTypes;
import cz.juzna.intellij.neon.parser.NeonParser;
import cz.juzna.intellij.neon.psi.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CompletionUtil {

	public static boolean isIncompleteKey(PsiElement el) {
		if (!NeonTokenTypes.STRING_LITERALS.contains(el.getNode().getElementType())) {
			return false;
		}
		//first scalar in file
		if (el.getParent() instanceof NeonScalar && el.getParent().getParent() instanceof NeonFile) {
			return true;
		}
		//error element
		if (el.getParent() instanceof NeonArray
			&& el.getPrevSibling() instanceof PsiErrorElement
			&& ((PsiErrorElement) el.getPrevSibling()).getErrorDescription().equals(NeonParser.EXPECTED_ARRAY_ITEM)) {
			return true;
		}
		//new key after new line
		if (el.getParent() instanceof NeonScalar
			&& (el.getParent().getParent() instanceof NeonKeyValPair | el.getParent().getParent().getNode().getElementType() == NeonElementTypes.ITEM)
			&& el.getParent().getPrevSibling().getNode().getElementType() == NeonTokenTypes.NEON_INDENT) {
			return true;
		}

		return false;
	}

	/**
	 * Get full name of property at given element (e.g. common.services.myService1.setup)
	 */
	public static NeonKeyChain getKeyChain(PsiElement el) {
		List<String> names = new ArrayList<String>();

		while (el != null) {
			if (el instanceof NeonKeyValPair) {
				names.add(0, ((NeonKeyValPair) el).getKeyText());
		} else if (el.getNode() != null && el.getNode().getElementType() == NeonElementTypes.ITEM) {
				names.add(0, "#");
			}

			el = el.getParent();
		}
		return NeonKeyChain.get(names.toArray(new String[names.size()]));
	}

	@Nullable
	public static NeonKeyValPair findCurrentKeyValuePair(PsiElement el) {
		if (el instanceof NeonKeyValPair) {
			return (NeonKeyValPair) el;
		}
		return PsiTreeUtil.getParentOfType(el, NeonKeyValPair.class);
	}

}
