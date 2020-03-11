package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyUsage;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
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
	private NeonPhpType phpType = null;
	//private NeonKeyChain currentKeyChain;

	public NeonKeyReference(@NotNull NeonKey element, TextRange textRange) {
		super(element, textRange);

		keyText = element.getKeyText();
		serviceDefinition = element.isServiceDefinition();
		parameterDefinition = element.isParameterDefinition();
		isArrayBullet = element.isArrayBullet();
		if (element.getParent() instanceof NeonKeyValPair && ((NeonKeyValPair) element.getParent()).getScalarValue() != null) {
			phpType = ((NeonKeyValPair) element.getParent()).getScalarValue().getPhpType();
		}
		//currentKeyChain = element.getKeyChain(false).withChildKey(element.getKeyText());
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<ResolveResult> results = new ArrayList<ResolveResult>();

		if (serviceDefinition) {
			NeonPhpUtil.attachNeonKeyUsages(keyText, results, getElement().getContainingFile());
			if (results.size() == 0 && phpType != null && isArrayBullet) {
				List<NeonKeyUsage> usages = NeonPhpUtil.attachNeonKeyUsagesByTypes(phpType, getElement().getProject());
				for (NeonKeyUsage usage : usages) {
					results.add(new PsiElementResolveResult(usage));
				}
			}

		} else if (parameterDefinition) {
			NeonPhpUtil.attachNeonParameterUsages(keyText, results, getElement().getContainingFile());
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