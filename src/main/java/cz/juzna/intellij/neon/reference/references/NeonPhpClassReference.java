package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import cz.juzna.intellij.neon.indexes.NeonIndexUtil;
import cz.juzna.intellij.neon.psi.NeonClassReference;
import cz.juzna.intellij.neon.psi.NeonClassUsage;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeonPhpClassReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private final String className;

	public NeonPhpClassReference(@NotNull NeonClassUsage element, TextRange textRange) {
		super(element, textRange);
		className = element.getClassName();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<ResolveResult> results = new ArrayList<ResolveResult>();
		Project project = getElement().getProject();
		for (PhpClass phpClass : NeonPhpUtil.getClassesByFQN(project, className)) {
			if (NeonPhpUtil.isReferenceFor(className, phpClass)) {
				results.add(new PsiElementResolveResult(phpClass));
			}
		}

		for (PhpNamespace phpNamespace : NeonPhpUtil.getNamespacesByName(project, className)) {
			results.add(new PsiElementResolveResult(phpNamespace));
		}

		for (NeonClassReference classReference : NeonIndexUtil.getClassesByFqn(project, className)) {
			results.add(new PsiElementResolveResult(classReference.getClassUsage()));
		}
		return results.toArray(new ResolveResult[0]);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length > 0 ? resolveResults[0].getElement() : null;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return new Object[0];
	}

	@Override
	public PsiElement handleElementRename(@NotNull String newName) {
		if (getElement() instanceof NeonClassUsage) {
			((NeonClassUsage) getElement()).setName(newName);
		}
		return getElement();
	}

	@Override
	public boolean isReferenceTo(@NotNull PsiElement element) {
		if (element instanceof NeonClassUsage) {
			return className.equals(((NeonClassUsage) element).getClassName());
		}

		if (element instanceof PhpClass) {
			return NeonPhpUtil.isReferenceFor(className, ((PhpClass) element));
		}

		if (!(element instanceof PhpNamespace)) {
			return false;
		}
		String namespace = ((PhpNamespace) element).getParentNamespaceName() + ((PhpNamespace) element).getName();
		return namespace.equals(className);
	}

}