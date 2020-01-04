package cz.juzna.intellij.neon.inspections;

import com.intellij.codeInspection.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.impl.NeonScalarImpl;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClassUsagesInspection extends LocalInspectionTool {

	@NotNull
	@Override
	public String getShortName() {
		return "ClassUsages";
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
				if (element instanceof NeonScalarImpl && ((NeonScalarImpl) element).isPhpScalar()) {
					String className = ((NeonScalarImpl) element).getName();
					if (NeonPhpUtil.getClassesByFQN(element.getProject(), className).size() == 0) {
						String description = "Undefined class '" + className + "'";
						ProblemDescriptor problem = manager.createProblemDescriptor(element, description, true, ProblemHighlightType.GENERIC_ERROR, isOnTheFly);
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
