package cz.juzna.intellij.neon.editor;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.psi.PsiElement;
import cz.juzna.intellij.neon.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class NeonStructureViewElement extends PsiTreeElementBase<PsiElement> {

	public NeonStructureViewElement(PsiElement element) {
		super(element);
	}

	@NotNull
	@Override
	public Collection<StructureViewTreeElement> getChildrenBase() {
		List<StructureViewTreeElement> elements = new ArrayList<StructureViewTreeElement>();
		PsiElement element = getElement();

		if (element instanceof NeonFile) {
			NeonPsiElement value = ((NeonFile) element).getValue();
			if (value instanceof NeonArray) { // top level array -> show it's elements
				addArrayElements(elements, value);
			} else if (!(value instanceof NeonScalar)) {
				// file children directly
				addArrayElements(elements, element);
			}

		} else if (element instanceof NeonKeyValPair && ((NeonKeyValPair) element).getValue() instanceof NeonArray) {
			addArrayElements(elements, ((NeonKeyValPair) element).getValue());

		} else if (element instanceof NeonArray) {
			addArrayElements(elements, element);

		}

		return elements;
	}

	private void addArrayElements(List<StructureViewTreeElement> elements, PsiElement firstValue) {
		for (PsiElement child : firstValue.getChildren()) {
			elements.add(new NeonStructureViewElement(child));
		}
	}

	@Override
	public String getPresentableText() {
		PsiElement element = getElement();
		if (element instanceof NeonFile) {
			return ((NeonFile) element).getName();

		} else if (element instanceof NeonArray) {
			return "array";

		} else if (element instanceof NeonKeyValPair) {
			return ((NeonKeyValPair) element).getKeyText();

		} else {
			return "?";
		}
	}

}
