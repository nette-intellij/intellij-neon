package cz.juzna.intellij.neon.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import cz.juzna.intellij.neon.config.NeonConfiguration;
import cz.juzna.intellij.neon.psi.*;
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
				if (element instanceof NeonKeyValPair) {
					NeonKey key = ((NeonKeyValPair) element).getKey();
					if (key == null) {
						super.visitElement(element);
						return;
					}

					Project project = key.getProject();
					if (key.isServiceDefinition() && key.getKeyChain(false).isLevel(1)) {
						if (key.isArrayBullet()) {
							return;
						}

						String serviceName = key.getKeyText();
						if (NeonConfiguration.INSTANCE.isStandardService(serviceName)) {
							return;
						}
						List<NeonKeyUsage> found = NeonPhpUtil.attachNeonKeyUsages(serviceName, project);

						if (found.size() == 0) {
							addDeprecated(manager, problems, key, "Service '" + serviceName + "' is not used by name", isOnTheFly);
							super.visitElement(element);
						}

					} else if (key.isParameterDefinition()) {
						String parameterName = key.getKeyChain(false).withoutParentKey().withChildKey(key.getKeyText()).toDottedString();

						List<NeonParameterUsage> found = NeonPhpUtil.attachNeonParameterUsages(key.getKeyChain(false).withChildKey(key.getKeyText()), project);

						if (found.size() == 0) {
							addDeprecated(manager, problems, key, "Parameter '" + parameterName + "' is not used", isOnTheFly);
							super.visitElement(element);

						} else {
							for (NeonParameterUsage usage : found) {
								if (usage.isLastKeyUsed()) {
									return;
								}
							}
						}

					} else {
						super.visitElement(element);
					}

				} else {
					super.visitElement(element);
				}
			}
		});

		return problems.toArray(new ProblemDescriptor[0]);
	}

}
