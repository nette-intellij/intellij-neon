package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonParameterUsage;
import cz.juzna.intellij.neon.psi.NeonScalar;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeonParameterUsageReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String keyText;

	public NeonParameterUsageReference(@NotNull NeonScalar element, TextRange textRange) {
		super(element, textRange);
		PsiElement parent = element.getParent();

		assert parent instanceof NeonParameterUsage;

		keyText = ((NeonParameterUsage) parent).getKeyText();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<ResolveResult> results = new ArrayList<ResolveResult>();

		List<NeonKey> keys = new ArrayList<NeonKey>();
		NeonPhpUtil.findNeonKeyDefinitions(keys, getElement().getContainingFile());
		for (NeonKey key : keys) {
			if (key.getKeyChain(false).withChildKey(key.getKeyText()).equalsWithoutMainKeyWithDots(keyText) && key.isParameterDefinition()) {
				results.add(new PsiElementResolveResult(key.getFirstChild()));
			}
		}

		return results.toArray(new ResolveResult[results.size()]);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return new Object[0];
	}

	@Override
	public boolean isReferenceTo(@NotNull PsiElement element) {
		if (element instanceof NeonScalar && element.getParent() instanceof NeonKey) {
			NeonKey key = (NeonKey) element.getParent();
			return key.getKeyChain(false).withChildKey(key.getKeyText()).equalsWithoutMainKeyWithDots(keyText) && key.isParameterDefinition();
		}
		return false;
	}

}