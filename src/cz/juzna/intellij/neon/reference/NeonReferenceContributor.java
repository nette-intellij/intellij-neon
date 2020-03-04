package cz.juzna.intellij.neon.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyUsage;
import cz.juzna.intellij.neon.psi.NeonParameterUsage;
import cz.juzna.intellij.neon.psi.NeonScalar;
import cz.juzna.intellij.neon.psi.impl.NeonScalarImpl;
import cz.juzna.intellij.neon.reference.references.NeonKeyReference;
import cz.juzna.intellij.neon.reference.references.NeonKeyUsageReference;
import cz.juzna.intellij.neon.reference.references.NeonParameterUsageReference;
import cz.juzna.intellij.neon.reference.references.NeonPhpClassReference;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
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
						if (name != null && name.length() > 0) {
							TextRange range = null;
							if (((NeonScalarImpl) element).isPhpScalar()) {
								range = new TextRange(0, name.length());
							} else if (name.startsWith("@") && name.length() > 1 && NeonPhpUtil.isPhpClassScalar(name.substring(1))) {
								range = new TextRange(1, name.length());
							}

							if (range == null) {
								return PsiReference.EMPTY_ARRAY;
							}

							return new PsiReference[]{
									new NeonPhpClassReference((NeonScalarImpl) element, range)
							};
						}
						return PsiReference.EMPTY_ARRAY;
					}
				});

		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement().withLanguage(NeonLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						if (!(element instanceof NeonScalar)) {
							return PsiReference.EMPTY_ARRAY;
						}

						PsiElement parent = element.getParent();
						if (!(parent instanceof NeonKey)) {
							return PsiReference.EMPTY_ARRAY;
						}

						String name = ((NeonKey) parent).getKeyText();
						if (name != null && name.length() > 0) {
							try {
								return new PsiReference[]{
										new NeonKeyReference((NeonScalar) element, new TextRange(0, name.length()))
								};
							} catch (AssertionError e) {
								return PsiReference.EMPTY_ARRAY;
							}
						}
						return PsiReference.EMPTY_ARRAY;
					}
				});

		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement().withLanguage(NeonLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						if (!(element instanceof NeonScalar)) {
							return PsiReference.EMPTY_ARRAY;
						}

						PsiElement parent = element.getParent();
						if (!(parent instanceof NeonKeyUsage)) {
							return PsiReference.EMPTY_ARRAY;
						}

						String name = ((NeonKeyUsage) parent).getKeyText();
						if (name != null && name.length() > 0) {
							try {
								return new PsiReference[]{
										new NeonKeyUsageReference((NeonScalar) element, new TextRange(1, name.length() + 1))
								};
							} catch (AssertionError e) {
								return PsiReference.EMPTY_ARRAY;
							}
						}
						return PsiReference.EMPTY_ARRAY;
					}
				});

		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement().withLanguage(NeonLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						if (!(element instanceof NeonScalar)) {
							return PsiReference.EMPTY_ARRAY;
						}

						PsiElement parent = element.getParent();
						if (!(parent instanceof NeonParameterUsage)) {
							return PsiReference.EMPTY_ARRAY;
						}

						String name = ((NeonParameterUsage) parent).getKeyText();
						if (name != null && name.length() > 0) {
							try {
								return new PsiReference[]{
										new NeonParameterUsageReference((NeonScalar) element, new TextRange(1, name.length() + 1))
								};
							} catch (AssertionError e) {
								return PsiReference.EMPTY_ARRAY;
							}
						}
						return PsiReference.EMPTY_ARRAY;
					}
				});
	}
}
