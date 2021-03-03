package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyUsage;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeonServiceUsageReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	private final String keyText;
	private final String className;

	public NeonServiceUsageReference(@NotNull NeonKeyUsage element, TextRange textRange) {
		super(element, textRange);
		keyText = element.getKeyText();
		className = element.getClassName();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<ResolveResult> results = new ArrayList<>();

		Project project = getElement().getProject();
		if (keyText.length() > 0) {
			List<NeonKey> keys = NeonPhpUtil.attachNeonKeyDefinitionsForServices(keyText, project);
			for (NeonKey key : keys) {
				results.add(new PsiElementResolveResult(key));
			}
		} else if (className.length() > 0) {
			List<NeonKeyValPair> keys = NeonPhpUtil.attachNeonKeyDefinitionsForServicesByClasses(className, project);
			for (NeonKeyValPair pair : keys) {
				if (pair.getKey() == null) {
					continue;
				}
				results.add(new PsiElementResolveResult(pair.getKey()));
			}
		}

		return results.toArray(new ResolveResult[0]);
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
		if (element instanceof NeonKey) {
			PsiElement parent = element.getParent();
			if (!(parent instanceof NeonKeyValPair)) {
				return false;
			}

			NeonKeyValPair pair = (NeonKeyValPair) parent;

			NeonKey key = (NeonKey) element;
			return key.isServiceDefinition() && (
					key.getKeyText().equals(keyText)
					|| (key.isArrayBullet() && pair.getScalarValue() != null && pair.getScalarValue().getPhpType().hasClass(className))
			);
		}
		return false;
	}

}