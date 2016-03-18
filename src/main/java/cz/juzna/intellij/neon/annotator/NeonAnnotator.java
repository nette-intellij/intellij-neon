package cz.juzna.intellij.neon.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonArray;
import cz.juzna.intellij.neon.psi.NeonKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NeonAnnotator implements Annotator {

	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		if (element.getNode().getElementType() == NeonTokenTypes.NEON_INDENT
				&& element.getPrevSibling() != null
				&& element.getPrevSibling() instanceof PsiErrorElement
				&& ((PsiErrorElement) element.getPrevSibling()).getErrorDescription().equals("Tab/space mixing")) {
			holder.createErrorAnnotation(element, "Tab/space mixing");
		} else if (element instanceof NeonArray) {
			List<NeonKey> arrayKeys = ((NeonArray) element).getKeys();
			Set<String> keys = new HashSet<String>(arrayKeys.size());
			for (NeonKey key : arrayKeys) {
				if (keys.contains(key.getKeyText())) {
					holder.createErrorAnnotation(key, "Duplicate key");
				} else {
					keys.add(key.getKeyText());
				}
			}
		}
	}
}
