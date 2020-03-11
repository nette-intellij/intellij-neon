package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyUsage;
import cz.juzna.intellij.neon.psi.NeonParameterUsage;
import cz.juzna.intellij.neon.util.NeonPhpType;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeonKeyReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String keyText;

	private boolean serviceDefinition;
	private boolean parameterDefinition;
	private boolean isArrayBullet;
	private NeonPhpType phpType;

	public NeonKeyReference(@NotNull NeonKey element, TextRange textRange) {
		super(element, textRange);

		keyText = element.getKeyText();
		serviceDefinition = element.isServiceDefinition();
		parameterDefinition = element.isParameterDefinition();
		isArrayBullet = element.isArrayBullet();
		phpType = element.getPhpType();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<ResolveResult> results = new ArrayList<ResolveResult>();

		Project project = getElement().getProject();
		if (serviceDefinition) {
			for (NeonKeyUsage usage : NeonPhpUtil.attachNeonKeyUsages(keyText, project)) {
				results.add(new PsiElementResolveResult(usage));
			}
			if (results.size() == 0 && isArrayBullet) {
				List<NeonKeyUsage> usages = NeonPhpUtil.attachNeonKeyUsagesByTypes(phpType, project);
				for (NeonKeyUsage usage : usages) {
					results.add(new PsiElementResolveResult(usage));
				}
			}

		} else if (parameterDefinition) {
			List<NeonParameterUsage> usages = NeonPhpUtil.attachNeonParameterUsages(keyText, project);
			for (NeonParameterUsage usage : usages) {
				results.add(new PsiElementResolveResult(usage));
			}
		}

		return results.toArray(new ResolveResult[results.size()]);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		return getElement(); // todo: complete resolving to definitions in DI extensions?
		//ResolveResult[] resolveResults = multiResolve(false);
		//return resolveResults.length > 0 ? resolveResults[0].getElement() : null;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return new Object[0];
	}

}