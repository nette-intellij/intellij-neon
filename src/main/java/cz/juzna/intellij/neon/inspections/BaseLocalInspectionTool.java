package cz.juzna.intellij.neon.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

abstract class BaseLocalInspectionTool extends LocalInspectionTool {

	protected void addProblem(
			@NotNull final InspectionManager manager,
			List<ProblemDescriptor> problems,
			@NotNull PsiElement element,
			@NotNull String description,
			boolean isOnTheFly
	) {
		addProblem(manager, problems, element, description, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, isOnTheFly);
	}

	protected void addUnused(
			@NotNull final InspectionManager manager,
			List<ProblemDescriptor> problems,
			@NotNull PsiElement element,
			@NotNull String description,
			boolean isOnTheFly
	) {
		addProblem(manager, problems, element, description, ProblemHighlightType.LIKE_UNUSED_SYMBOL, isOnTheFly);
	}

	protected void addWeakWarning(
			@NotNull final InspectionManager manager,
			List<ProblemDescriptor> problems,
			@NotNull PsiElement element,
			@NotNull String description,
			boolean isOnTheFly
	) {
		addProblem(manager, problems, element, description, ProblemHighlightType.INFORMATION, isOnTheFly);
	}

	protected void addProblem(
			@NotNull final InspectionManager manager,
			List<ProblemDescriptor> problems,
			@NotNull PsiElement element,
			@NotNull String description,
			@NotNull ProblemHighlightType type,
			boolean isOnTheFly
	) {
		ProblemDescriptor problem = manager.createProblemDescriptor(element, description, true, type, isOnTheFly);
		problems.add(problem);
	}
}