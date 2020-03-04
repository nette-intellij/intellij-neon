package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import cz.juzna.intellij.neon.config.NeonKeyChain;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyUsage;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import cz.juzna.intellij.neon.psi.NeonScalar;
import cz.juzna.intellij.neon.psi.impl.NeonScalarImpl;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeonKeyReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String keyText;

	private boolean serviceDefinition;
	private boolean parameterDefinition;

	public NeonKeyReference(@NotNull NeonScalar element, TextRange textRange) {
		super(element, textRange);
		PsiElement parent = element.getParent();

		assert parent instanceof NeonKey;

		keyText = ((NeonKey) parent).getKeyText();
		serviceDefinition = ((NeonKey) parent).isServiceDefinition();
		parameterDefinition = ((NeonKey) parent).isParameterDefinition();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<ResolveResult> results = new ArrayList<ResolveResult>();

		if (serviceDefinition) {
			NeonPhpUtil.attachNeonKeyUsages(keyText, results, getElement().getContainingFile());

		} else if (parameterDefinition) {
			NeonPhpUtil.attachNeonParameterUsages(keyText, results, getElement().getContainingFile());
		}

		return results.toArray(new ResolveResult[results.size()]);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		return getElement(); // todo: complete resolving to definitions int DI extensions?
		//ResolveResult[] resolveResults = multiResolve(false);
		//return resolveResults.length > 0 ? resolveResults[0].getElement() : null;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return new Object[0];
	}

}