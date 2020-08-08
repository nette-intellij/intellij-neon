package cz.juzna.intellij.neon.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import cz.juzna.intellij.neon.config.NeonConfiguration;
import cz.juzna.intellij.neon.config.NeonService;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.NeonKeyUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ServiceUsagesInspection extends BaseLocalInspectionTool {

	@NotNull
	@Override
	public String getShortName() {
		return "NeonServiceUsages";
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
				if (element instanceof NeonKeyUsage) {
					NeonKeyUsage keyUsage = (NeonKeyUsage) element;
					String serviceName = keyUsage.getClassReference() != null ? keyUsage.getClassName() : keyUsage.getKeyText();
					NeonService service = NeonConfiguration.INSTANCE.findService(serviceName, element.getProject());
					if (service == null) {
						addProblem(manager, problems, element, "Service '" + serviceName + "' not found", isOnTheFly);
					}

				} else {
					super.visitElement(element);
				}
			}
		});

		return problems.toArray(new ProblemDescriptor[problems.size()]);
	}

}
