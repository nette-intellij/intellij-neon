package cz.juzna.intellij.neon.inspections;

import com.intellij.codeInspection.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import cz.juzna.intellij.neon.psi.NeonClassUsage;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassUsagesInspection extends LocalInspectionTool {

	@NotNull
	@Override
	public String getShortName() {
		return "NeonClassUsages";
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
				if (element instanceof NeonClassUsage) {
					String className = ((NeonClassUsage) element).getClassName();
					Project project = element.getProject();
					boolean isValid = true;
					if (NeonPhpUtil.getClassesByFQN(project, className).size() == 0) {
						isValid = false;

						Collection<PhpNamespace> namespaces = NeonPhpUtil.getNamespacesByName(project, className);
						if (namespaces.size() > 0) {
							isValid = true;
						}
					}

					if (!isValid) {
						String description = "Undefined class '" + className + "'";
						ProblemDescriptor problem = manager.createProblemDescriptor(element.getParent(), description, true, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, isOnTheFly);
						problems.add(problem);
					}

				} else {
					super.visitElement(element);
				}
			}
		});

		return problems.toArray(new ProblemDescriptor[problems.size()]);
	}

}
