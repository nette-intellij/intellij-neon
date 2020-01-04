package cz.juzna.intellij.neon.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.ExtendsList;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import cz.juzna.intellij.neon.psi.impl.NeonScalarImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NeonPhpUtil {
	public static boolean isReferenceFor(@NotNull String originalClass, @NotNull PhpClass targetClass) {
		originalClass = NeonPhpUtil.normalizeClassName(originalClass);
		if (originalClass.equals(targetClass.getFQN())) {
			return true;
		}

		ExtendsList extendsList = targetClass.getExtendsList();
		for (ClassReference reference : extendsList.getReferenceElements()) {
			if (reference.getFQN().equals(originalClass)) {
				return true;
			}
		}
		return false;
	}

	public static void findNeonPhpClasses(List<NeonScalarImpl> properties, PsiElement psiElement) {
		for (PsiElement element : collectPsiElementsRecursive(psiElement)) {
			if (isPhpClassScalar(element)) {
				properties.add((NeonScalarImpl) element);
			}
		}
	}

	public static Collection<PhpClass> getClassesByFQN(Project project, String className) {
		return getPhpIndex(project).getAnyByFQN(className);
	}

	private static PhpIndex getPhpIndex(Project project) {
		return PhpIndex.getInstance(project);
	}

	public static boolean isPhpClassScalar(@NotNull PsiElement element) {
		if (!(element instanceof NeonScalarImpl)) {
			return false;
		}

		String name = ((NeonScalarImpl) element).getName();
		return name != null && name.matches("[a-zA-Z_]*\\\\[a-zA-Z0-9_\\\\]*");
	}

	@NotNull
	private static List<PsiElement> collectPsiElementsRecursive(@NotNull PsiElement psiElement) {
		final List<PsiElement> elements = new ArrayList<PsiElement>();
		elements.add(psiElement.getContainingFile());

		psiElement.acceptChildren(new PsiRecursiveElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				elements.add(element);
				super.visitElement(element);
			}
		});
		return elements;
	}

	private static String normalizeClassName(@NotNull String className) {
		return className.startsWith("\\") ? className : ("\\" + className);
	}
}
