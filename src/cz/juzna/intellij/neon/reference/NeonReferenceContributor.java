package cz.juzna.intellij.neon.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.psi.*;
import cz.juzna.intellij.neon.reference.references.*;
import cz.juzna.intellij.neon.util.NeonUtil;
import org.jetbrains.annotations.NotNull;

public class NeonReferenceContributor extends PsiReferenceContributor {
	public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement(NeonClassUsage.class).withLanguage(NeonLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						if (!(element instanceof NeonClassUsage)) {
							return PsiReference.EMPTY_ARRAY;
						}

						String name = element.getText();
						if (name != null && name.length() > 0) {
							TextRange range = new TextRange(name.startsWith("\\") && name.length() > 1 ? 1 : 0, name.length());

							return new PsiReference[]{
									new NeonPhpClassReference((NeonClassUsage) element, range)
							};
						}
						return PsiReference.EMPTY_ARRAY;
					}
				});

		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement(NeonNamespaceReference.class).withLanguage(NeonLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						if (!(element instanceof NeonNamespaceReference)) {
							return PsiReference.EMPTY_ARRAY;
						}

						String name = element.getText();
						if (name != null && name.length() > 0) {
							return new PsiReference[]{
									new NeonPhpNamespaceReference((NeonNamespaceReference) element, new TextRange(0, name.length()))
							};
						}
						return PsiReference.EMPTY_ARRAY;
					}
				});

		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement(NeonKey.class).withLanguage(NeonLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						if (!(element instanceof NeonKey)) {
							return PsiReference.EMPTY_ARRAY;
						}

						String normalizedName = NeonUtil.normalizeKeyName(element.getText());
						String trimName = NeonUtil.normalizeKeyName(element.getText().trim());
						int start = normalizedName.length() - trimName.length();

						String name = ((NeonKey) element).getName();
						if (name != null && normalizedName.length() > start) {
							try {
								return new PsiReference[]{
										new NeonKeyReference((NeonKey) element, new TextRange(start, normalizedName.length()))
								};
							} catch (AssertionError e) {
								return PsiReference.EMPTY_ARRAY;
							}
						}
						return PsiReference.EMPTY_ARRAY;
					}
				});

		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement(NeonKeyUsage.class).withLanguage(NeonLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						if (!(element instanceof NeonKeyUsage)) {
							return PsiReference.EMPTY_ARRAY;
						}

						String name = ((NeonKeyUsage) element).getKeyText();
						if (name != null) {
							try {
								TextRange range = null;
								if (name.length() > 0) {
									range = new TextRange(1, name.length() + 1);
								} else if (element.getText().length() > 0) {
									range = new TextRange(0, 1);
								}

								if (range == null) {
									return PsiReference.EMPTY_ARRAY;
								}

								return new PsiReference[]{
										new NeonServiceUsageReference((NeonKeyUsage) element, range)
								};
							} catch (AssertionError e) {
								return PsiReference.EMPTY_ARRAY;
							}
						}
						return PsiReference.EMPTY_ARRAY;
					}
				});

		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement(NeonParameterUsage.class).withLanguage(NeonLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						if (!(element instanceof NeonParameterUsage)) {
							return PsiReference.EMPTY_ARRAY;
						}

						String name = ((NeonParameterUsage) element).getKeyText();
						if (name != null && name.length() > 0) {
							try {
								return new PsiReference[]{
										new NeonParameterUsageReference((NeonParameterUsage) element, new TextRange(1, name.length() + 1))
								};
							} catch (AssertionError e) {
								return PsiReference.EMPTY_ARRAY;
							}
						}
						return PsiReference.EMPTY_ARRAY;
					}
				});

		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement(NeonMethodUsage.class).withLanguage(NeonLanguage.INSTANCE),
				new PsiReferenceProvider() {
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
						if (!(element instanceof NeonMethodUsage)) {
							return PsiReference.EMPTY_ARRAY;
						}

						String name = ((NeonMethodUsage) element).getMethodName();
						if (name != null && name.length() > 0) {
							try {
								return new PsiReference[]{
										new NeonPhpMethodReference((NeonMethodUsage) element, new TextRange(0, name.length()))
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
