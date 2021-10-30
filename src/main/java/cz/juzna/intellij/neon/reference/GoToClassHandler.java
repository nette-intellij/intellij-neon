package cz.juzna.intellij.neon.reference;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import cz.juzna.intellij.neon.psi.elements.NeonScalar;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class GoToClassHandler implements GotoDeclarationHandler {

	@Nullable
	@Override
	public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement element, int offset, Editor editor) {
		if (element == null || element.getParent() == null || !(element.getParent() instanceof NeonScalar)) {
			return new PsiElement[0];
		}
		PhpIndex phpIndex = PhpIndex.getInstance(element.getProject());
		Collection<PhpClass> classes = phpIndex.getAnyByFQN(((NeonScalar) element.getParent()).getValueText());

		return classes.toArray(new PsiElement[classes.size()]);
	}

	@Nullable
	@Override
	public String getActionText(DataContext context) {
		return null;
	}

}
