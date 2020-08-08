package cz.juzna.intellij.neon.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.NeonMethodUsage;
import cz.juzna.intellij.neon.util.NeonPhpType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MethodUsagesInspection extends BaseLocalInspectionTool {

	@NotNull
	@Override
	public String getShortName() {
		return "NeonMethodUsages";
	}

	@Nullable
	@Override
	public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull final InspectionManager manager, final boolean isOnTheFly) {
		if (!(file instanceof NeonFile)) {
			return null;
		}

		final List<ProblemDescriptor> problems = new ArrayList<ProblemDescriptor>();
		file.acceptChildren(new PsiRecursiveElementWalkingVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				if (element instanceof NeonMethodUsage) {
					NeonPhpType phpType = ((NeonMethodUsage) element).getPhpType();

					boolean isFound = false;
					Collection<PhpClass> phpClasses = phpType.getPhpClasses(element.getProject());
					String methodName = ((NeonMethodUsage) element).getMethodName();
					if (phpClasses != null) {
						for (PhpClass phpClass : phpClasses) {
							for (Method method : phpClass.getMethods()) {
								if (method.getName().equals(methodName)) {
									if (method.getModifier().isPrivate()) {
										addProblem(manager, problems, element, "Used private method '" + methodName + "'", isOnTheFly);

									} else if (method.getModifier().isProtected()) {
										addProblem(manager, problems, element, "Used protected method '" + methodName + "'", isOnTheFly);
									}

									isFound = true;
								}
							}
						}
					}

					if (!isFound) {
						addProblem(manager, problems, element, "Method '" + methodName + "' not found", ProblemHighlightType.GENERIC_ERROR_OR_WARNING, isOnTheFly);
					}

				} else {
					super.visitElement(element);
				}
			}
		});

		return problems.toArray(new ProblemDescriptor[problems.size()]);
	}

}
