package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import cz.juzna.intellij.neon.psi.NeonScalar;
import cz.juzna.intellij.neon.psi.impl.NeonScalarImpl;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeonPhpClassReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String className;

	public NeonPhpClassReference(@NotNull NeonScalarImpl element, TextRange textRange) {
		super(element, textRange);
		className = element.getNormalizedClassName();
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

		List<NeonScalarImpl> classes = new ArrayList<NeonScalarImpl>();
		NeonPhpUtil.findNeonPhpClasses(classes, getElement().getContainingFile());
		for (NeonScalarImpl method : classes) {
			results.add(new PsiElementResolveResult(method));
		}

		return results.toArray(new ResolveResult[results.size()]);
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
		if (getElement() instanceof NeonScalarImpl && ((NeonScalarImpl) getElement()).isPhpScalar()) {
			((NeonScalarImpl) getElement()).setName(newName);
		}
		return getElement();
	}

	@Override
	public boolean isReferenceTo(@NotNull PsiElement element) {
		if (element instanceof NeonScalar) {
			return className.equals(((NeonScalar) element).getName());
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