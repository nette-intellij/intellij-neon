package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import cz.juzna.intellij.neon.indexes.NeonIndexUtil;
import cz.juzna.intellij.neon.psi.NeonConstantUsage;
import cz.juzna.intellij.neon.psi.NeonMethodUsage;
import cz.juzna.intellij.neon.util.NeonPhpType;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NeonPhpConstantReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private final String constantName;
	private final NeonPhpType phpType;
	private final Project project;

	public NeonPhpConstantReference(@NotNull NeonConstantUsage element, TextRange textRange) {
		super(element, textRange);
		constantName = element.getConstantName();
		phpType = element.getPhpType();
		project = element.getProject();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<ResolveResult> results = new ArrayList<>();

		for (PhpTypedElement reference : NeonPhpUtil.getReferencedElements(phpType, project, constantName)) {
			results.add(new PsiElementResolveResult(reference));
		}

		final Collection<NeonConstantUsage> usages = NeonIndexUtil.findConstantsByName(getElement().getProject(), constantName);
		for (NeonConstantUsage usage : usages) {
			if (usage.getPhpType().hasClass(phpType.getPhpClasses(project))) {
				results.add(new PsiElementResolveResult(usage));
			}
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
		if (getElement() instanceof NeonMethodUsage) {
			((NeonMethodUsage) getElement()).setName(newName);
		}
		return getElement();
	}

	@Override
	public boolean isReferenceTo(@NotNull PsiElement element) {
		if (element instanceof NeonConstantUsage) {
			return ((NeonConstantUsage) element).getConstantName().equals(constantName)
					&& phpType.hasClass(((NeonMethodUsage) element).getPhpType().getPhpClasses(project));
		} else if (element instanceof Field) {
			PhpClass originalClass = ((Field) element).getContainingClass();
			if (originalClass == null) {
				return false;
			}
			return NeonPhpUtil.isReferenceTo(originalClass, multiResolve(false), element, ((Field) element).getName());

		} else if (element instanceof Method) {
			PhpClass originalClass = ((Method) element).getContainingClass();
			if (originalClass == null) {
				return false;
			}
			for (Field field : originalClass.getFields()) {
				if (field.getModifier().isPublic() && field.isConstant() && field.getName().equals(constantName)) {
					return false;
				}
			}

			return NeonPhpUtil.isReferenceTo(originalClass, multiResolve(false), element, ((Method) element).getName());
		}
		return false;
	}

}