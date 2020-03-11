package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import cz.juzna.intellij.neon.config.NeonConfiguration;
import cz.juzna.intellij.neon.config.NeonService;
import cz.juzna.intellij.neon.psi.NeonClassUsage;
import cz.juzna.intellij.neon.psi.NeonMethodUsage;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeonPhpMethodReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String methodName;
	private NeonService service;
	private Project project;

	public NeonPhpMethodReference(@NotNull NeonMethodUsage element, TextRange textRange) {
		super(element, textRange);
		methodName = element.getMethodName();
		service = NeonConfiguration.INSTANCE.findService(element.getServiceName(), element.getProject());
		project = element.getProject();
		assert service != null;
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<ResolveResult> results = new ArrayList<ResolveResult>();

		for (PhpClass phpClass : service.getPhpType().getPhpClasses(project)) {
			for (Method method : phpClass.getMethods()) {
				if (method.getModifier().isPublic() && !method.isStatic() && method.getName().equals(methodName)) {
					results.add(new PsiElementResolveResult(method));
				}
			}
		}

		List<NeonMethodUsage> usages = NeonPhpUtil.findNeonMethodUsages(service.getPhpType(), methodName, getElement().getProject());
		for (NeonMethodUsage usage : usages) {
			results.add(new PsiElementResolveResult(usage));
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
		if (getElement() instanceof NeonMethodUsage) {
			((NeonMethodUsage) getElement()).setName(newName);
		}
		return getElement();
	}

	@Override
	public boolean isReferenceTo(@NotNull PsiElement element) {
		if (element instanceof NeonMethodUsage) {
			return ((NeonMethodUsage) element).getMethodName().equals(methodName)
					&& service.getPhpType().hasClass(((NeonMethodUsage) element).getPhpType().getPhpClasses(project));
		}

		if (element instanceof Method) {
			PhpClass originalClass = ((Method) element).getContainingClass();
			if (originalClass == null) {
				return false;
			}
			return NeonPhpUtil.isReferenceTo(originalClass, multiResolve(false), element, ((Method) element).getName());
		}
		return false;
	}

}