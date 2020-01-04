package cz.juzna.intellij.neon.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.psi.impl.NeonScalarImpl;
import cz.juzna.intellij.neon.reference.references.NeonPhpClassReference;
import org.jetbrains.annotations.NotNull;

public class NeonReferenceContributor extends PsiReferenceContributor {
	public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement().withLanguage(NeonLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						if (!(element instanceof NeonScalarImpl)) {
							return PsiReference.EMPTY_ARRAY;
						}

						String name = ((NeonScalarImpl) element).getName();
						if (name != null && ((NeonScalarImpl) element).isPhpScalar()) {
							return new PsiReference[]{
									new NeonPhpClassReference((NeonScalarImpl) element, new TextRange(0, name.length() + 1))};
						}
						return PsiReference.EMPTY_ARRAY;
					}
				});
	}
}
