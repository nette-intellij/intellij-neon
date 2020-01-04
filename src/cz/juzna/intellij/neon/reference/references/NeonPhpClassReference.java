package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.jetbrains.php.lang.psi.elements.PhpClass;
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
		className = element.getName();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<NeonScalarImpl> classes = new ArrayList<NeonScalarImpl>();
		NeonPhpUtil.findNeonPhpClasses(classes, getElement().getContainingFile());
		if (classes.size() == 0) {
			return new ResolveResult[0];
		}

		List<ResolveResult> results = new ArrayList<ResolveResult>();
		for (NeonScalarImpl method : classes) {
			results.add(new PsiElementResolveResult(method));
		}

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
	public boolean isReferenceTo(@NotNull PsiElement element) {
		if (element instanceof NeonScalar) {
			return className.equals(((NeonScalar) element).getName());
		}

		if (!(element instanceof PhpClass)) {
			return false;
		}
		return NeonPhpUtil.isReferenceFor(className, ((PhpClass) element));
	}

}