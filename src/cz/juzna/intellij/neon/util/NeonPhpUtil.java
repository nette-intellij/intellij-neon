package cz.juzna.intellij.neon.util;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.ResolveResult;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.ExtendsList;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyUsage;
import cz.juzna.intellij.neon.psi.NeonParameterUsage;
import cz.juzna.intellij.neon.psi.impl.NeonScalarImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
			if (reference.getFQN() == null) {
				continue;
			}

			if (reference.getFQN().equals(originalClass)) {
				return true;
			}
		}
		return false;
	}

	public static void findNeonPhpClasses(List<NeonScalarImpl> properties, PsiElement psiElement) {
		psiElement.acceptChildren(new PsiRecursiveElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				if (isPhpClassScalar(element)) {
					properties.add((NeonScalarImpl) element);
				} else {
					super.visitElement(element);
				}
			}
		});
	}

	public static void findNeonKeyDefinitions(List<NeonKey> properties, PsiElement psiElement) {
		psiElement.acceptChildren(new PsiRecursiveElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				if (element.getParent() instanceof NeonKey) {
					properties.add((NeonKey) element.getParent());
				} else {
					super.visitElement(element);
				}
			}
		});
	}

	public static void attachNeonKeyUsages(String searchedName, List<ResolveResult> results, PsiElement psiElement) {
		psiElement.acceptChildren(new PsiRecursiveElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				if (element.getParent() instanceof NeonKeyUsage && ((NeonKeyUsage) element.getParent()).getKeyText().equals(searchedName)) {
					results.add(new PsiElementResolveResult(element.getParent()));
				} else {
					super.visitElement(element);
				}
			}
		});
	}

	public static void attachNeonParameterUsages(String searchedName, List<ResolveResult> results, PsiElement psiElement) {
		psiElement.acceptChildren(new PsiRecursiveElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				if (element.getParent() instanceof NeonParameterUsage && ((NeonParameterUsage) element.getParent()).getKeyText().equals(searchedName)) {
					results.add(new PsiElementResolveResult(element.getParent()));
				} else {
					super.visitElement(element);
				}
			}
		});
	}

	public static Collection<PhpClass> getClassesByFQN(Project project, String className) {
		return getPhpIndex(project).getAnyByFQN(className);
	}

	public static Collection<PhpNamespace> getNamespacesByName(Project project, String className) {
		return getPhpIndex(project).getNamespacesByName(className);
	}

	private static PhpIndex getPhpIndex(Project project) {
		return PhpIndex.getInstance(project);
	}

	public static boolean isPhpClassScalar(@NotNull PsiElement element) {
		if (!(element instanceof NeonScalarImpl)) {
			return false;
		}

		String name = ((NeonScalarImpl) element).getName();
		return isPhpClassScalar(name);
	}

	public static boolean isPhpClassScalar(@Nullable String elementName) {
		return elementName != null && elementName.matches("[a-zA-Z_]*\\\\[a-zA-Z0-9_\\\\]*");
	}

	@NotNull
	public static PsiElement replaceChildNode(@NotNull PsiElement psiElement, @NotNull PsiElement newElement, @Nullable ASTNode keyNode) {
		ASTNode newKeyNode = newElement.getFirstChild().getNode();
		if (newKeyNode == null) {
			return psiElement;
		}

		if (keyNode == null) {
			psiElement.getNode().addChild(newKeyNode);

		} else {
			psiElement.getNode().replaceChild(keyNode, newKeyNode);
		}
		return psiElement;
	}

	public static String normalizeClassName(@Nullable String className) {
		className = className == null ? "" : (className.startsWith("@") ? className.substring(1) : className);
		return className.startsWith("\\") ? className : ("\\" + className);
	}
}
