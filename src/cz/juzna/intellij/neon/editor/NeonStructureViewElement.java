package cz.juzna.intellij.neon.editor;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.psi.PsiElement;
import cz.juzna.intellij.neon.NeonIcons;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.NeonScalarValue;
import cz.juzna.intellij.neon.psi.*;
import cz.juzna.intellij.neon.psi.elements.NeonArrayElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
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
			PsiElement value = ((NeonFile) element).getValue();
			if (value instanceof NeonArrayElement) { // top level array -> show it's elements
				addArrayElements(elements, value);
			} else if (!(value instanceof NeonScalarValue)) {
				// file children directly
				addArrayElements(elements, element);
			}

		} else if (element instanceof NeonKeyValPair && ((NeonKeyValPair) element).getArray() != null) {
			addArrayElements(elements, ((NeonKeyValPair) element).getArray());

		} else if (element instanceof NeonArrayElement) {
			addArrayElements(elements, element);

		}

		return elements;
	}

	@Override
	public Icon getIcon(boolean open) {
		PsiElement element = getElement();
		if (element instanceof NeonFile) {
			return NeonIcons.FILETYPE_ICON;
		}
		return null;
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

		} else if (element instanceof NeonArrayElement) {
			return "array";

		} else if (element instanceof NeonKeyValPair) {
			return ((NeonKeyValPair) element).getKeyText();

		} else {
			return "?";
		}
	}

}
