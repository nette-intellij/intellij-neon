package cz.juzna.intellij.neon.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import org.jetbrains.annotations.NotNull;

public class NeonAnnotator implements Annotator {

	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		if (element.getNode().getElementType() == NeonTokenTypes.NEON_INDENT
				&& element.getPrevSibling() != null
				&& element.getPrevSibling() instanceof PsiErrorElement
				&& ((PsiErrorElement) element.getPrevSibling()).getErrorDescription().equals("Tab/space mixing")) {
			holder.createErrorAnnotation(element, "Tab/space mixing");
		}
	}
}
