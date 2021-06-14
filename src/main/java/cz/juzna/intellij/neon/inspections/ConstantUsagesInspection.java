package cz.juzna.intellij.neon.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import cz.juzna.intellij.neon.psi.NeonConstantUsage;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.NeonMethodUsage;
import cz.juzna.intellij.neon.util.NeonPhpType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConstantUsagesInspection extends BaseLocalInspectionTool {

	@NotNull
	@Override
	public String getShortName() {
		return "NeonConstantUsages";
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
				if (element instanceof NeonConstantUsage) {
					NeonPhpType phpType = ((NeonConstantUsage) element).getPhpType();

					boolean isFound = false;
					Collection<PhpClass> phpClasses = phpType.getPhpClasses(element.getProject());
					String constantName = ((NeonConstantUsage) element).getConstantName();
					if (phpClasses != null) {
						for (PhpClass phpClass : phpClasses) {
							for (Field field : phpClass.getFields()) {
								if (field.isConstant() && field.getName().equals(constantName)) {
									if (field.getModifier().isPrivate()) {
										addProblem(manager, problems, element, "Used private constant '" + constantName + "'", isOnTheFly);

									} else if (field.getModifier().isProtected()) {
										addProblem(manager, problems, element, "Used protected constant '" + constantName + "'", isOnTheFly);
									}

									isFound = true;
								}
							}

							if (!isFound) {
								for (Method method : phpClass.getMethods()) {
									if (method.getName().equals(constantName)) {
										if (method.getModifier().isPrivate()) {
											addProblem(manager, problems, element, "Used private method '" + constantName + "'", isOnTheFly);

										} else if (method.getModifier().isProtected()) {
											addProblem(manager, problems, element, "Used protected method '" + constantName + "'", isOnTheFly);
										}

										isFound = true;
									}
								}
							}
						}
					}

					if (!isFound) {
						addProblem(manager, problems, element, "Constant '" + constantName + "' not found", ProblemHighlightType.GENERIC_ERROR_OR_WARNING, isOnTheFly);
					}

				} else {
					super.visitElement(element);
				}
			}
		});

		return problems.toArray(new ProblemDescriptor[problems.size()]);
	}

}
