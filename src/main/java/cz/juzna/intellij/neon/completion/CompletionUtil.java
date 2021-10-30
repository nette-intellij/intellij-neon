package cz.juzna.intellij.neon.completion;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.parser.NeonElementTypes;
import cz.juzna.intellij.neon.parser.NeonParser;
import cz.juzna.intellij.neon.psi.elements.NeonArray;
import cz.juzna.intellij.neon.psi.elements.NeonFile;
import cz.juzna.intellij.neon.psi.elements.NeonKeyValPair;
import cz.juzna.intellij.neon.psi.elements.NeonScalar;

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

}
