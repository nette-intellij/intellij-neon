package cz.juzna.intellij.neon.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonArray;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import cz.juzna.intellij.neon.psi.NeonScalarValue;
import cz.juzna.intellij.neon.psi.elements.NeonArrayElement;
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
		} else if (element instanceof NeonKeyValPair) {
			NeonScalarValue scalarValue = ((NeonKeyValPair) element).getScalarValue();
			NeonArray array = ((NeonKeyValPair) element).getArray();
			if (array != null && scalarValue != null && !array.isEmpty()) {
				holder.createErrorAnnotation(scalarValue, "Can not use scalar value and array simultaneously");
			}/* else if (array == null && scalarValue == null) {
				holder.createErrorAnnotation(element, "Can not use key without value");
			}*/

		} else if (element instanceof NeonArrayElement) {
			List<NeonKey> arrayKeys = ((NeonArrayElement) element).getKeys();
			Set<String> keys = new HashSet<String>(arrayKeys.size());
			boolean arrayBulletOnly = true;
			boolean arrayBulletUsed = false;
			for (NeonKey key : arrayKeys) {
				if (key.isArrayBullet()) {
					arrayBulletUsed = true;
					continue;
				}

				arrayBulletOnly = false;
				if (keys.contains(key.getKeyText())) {
					holder.createErrorAnnotation(key, "Duplicate key");
				} else {
					keys.add(key.getKeyText());
				}
			}

			if (arrayBulletUsed && !arrayBulletOnly) {
				for (NeonKey key : arrayKeys) {
					//holder.createErrorAnnotation(key, "Mixing '-' and 'key: value' pairs");
				}
			}
		}
	}
}
