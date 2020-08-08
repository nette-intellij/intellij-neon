package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonParameterUsage;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeonParameterUsageReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private String keyText;

	public NeonParameterUsageReference(@NotNull NeonParameterUsage element, TextRange textRange) {
		super(element, textRange);

		keyText = element.getKeyText();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<ResolveResult> results = new ArrayList<ResolveResult>();

		Project project = getElement().getProject();
		List<NeonKey> definitions = NeonPhpUtil.attachNeonKeyDefinitionsForParameters(keyText, project);
		for (NeonKey key : definitions) {
			results.add(new PsiElementResolveResult(key));
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
	public PsiElement handleElementRename(@NotNull String newName) {
		if (getElement() instanceof NeonParameterUsage) {
			((NeonParameterUsage) getElement()).setName(newName);
		}
		return getElement();
	}

	@Override
	public boolean isReferenceTo(@NotNull PsiElement element) {
		if (element instanceof NeonParameterUsage) {
			return ((NeonParameterUsage) element).getKeyText().equals(keyText);
		}

		if (element instanceof NeonKey) {
			NeonKey key = ((NeonKey) element);
			return key.isParameterDefinition() && key.getKeyChain(false).withChildKey(key.getKeyText()).equalsWithoutMainKeyWithDots(keyText);
		}
		return false;
	}

}