package cz.juzna.intellij.neon.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyUsage;
import cz.juzna.intellij.neon.psi.NeonParameterUsage;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeonUnusedKeyInspection extends BaseLocalInspectionTool {

	@NotNull
	@Override
	public String getShortName() {
		return "NeonUnusedKey";
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
				if (element instanceof NeonKey) {
					NeonKey key = (NeonKey) element;

					Project project = key.getProject();
					if (key.isServiceDefinition() && key.getKeyChain(false).isLevel(1)) {
						if (key.isArrayBullet()) {
							return;
						}

						String serviceName = ((NeonKey) element).getKeyText();
						List<NeonKeyUsage> found = NeonPhpUtil.attachNeonKeyUsages(serviceName, project);

						if (found.size() == 0) {
							addDeprecated(manager, problems, element, "Service '" + serviceName + "' is not used by name", isOnTheFly);
						}

					} else if (key.isParameterDefinition()) {
						String parameterName = key.getKeyChain(false).withoutParentKey().withChildKey(key.getKeyText()).toDottedString();

						List<NeonParameterUsage> found = NeonPhpUtil.attachNeonParameterUsages(key.getKeyChain(false).withChildKey(key.getKeyText()), project);

						if (found.size() == 0 || (found.size() == 1 && found.get(0) == key)) {
							addDeprecated(manager, problems, element, "Parameter '" + parameterName + "' is not used", isOnTheFly);
						}
					}

				} else {
					super.visitElement(element);
				}
			}
		});

		return problems.toArray(new ProblemDescriptor[0]);
	}

}
