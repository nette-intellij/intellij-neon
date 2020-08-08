package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import cz.juzna.intellij.neon.psi.NeonNamespaceReference;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeonPhpNamespaceReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String namespaceName;

	public NeonPhpNamespaceReference(@NotNull NeonNamespaceReference element, TextRange textRange) {
		super(element, textRange);
		namespaceName = element.getNamespaceName();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<ResolveResult> results = new ArrayList<ResolveResult>();
		Project project = getElement().getProject();
		for (PhpNamespace phpNamespace : NeonPhpUtil.getNamespacesByName(project, namespaceName)) {
			results.add(new PsiElementResolveResult(phpNamespace));
		}

		NeonPhpUtil.attachNeonPhpNamespaces(namespaceName, results, getElement().getProject());

		return results.toArray(new ResolveResult[results.size()]);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return new Object[0];
	}

	@Override
	public PsiElement handleElementRename(@NotNull String newName) {
		if (getElement() instanceof NeonNamespaceReference) {
			((NeonNamespaceReference) getElement()).setName(newName);
		}
		return getElement();
	}

	@Override
	public boolean isReferenceTo(@NotNull PsiElement element) {
		if (element instanceof NeonNamespaceReference) {
			return namespaceName.equals(((NeonNamespaceReference) element).getNamespaceName());
		}

		if (!(element instanceof PhpNamespace)) {
			return false;
		}
		String namespace = ((PhpNamespace) element).getParentNamespaceName() + ((PhpNamespace) element).getName();
		return namespace.equals(namespaceName);
	}

}