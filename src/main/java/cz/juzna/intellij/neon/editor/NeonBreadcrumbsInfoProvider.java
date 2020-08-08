package cz.juzna.intellij.neon.editor;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.xml.breadcrumbs.BreadcrumbsInfoProvider;
import com.jetbrains.php.PhpIcons;
import cz.juzna.intellij.neon.NeonIcons;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Breadcrumbs info about which section are we editing now (just above the editor, below tabs)
 */
public class NeonBreadcrumbsInfoProvider extends BreadcrumbsInfoProvider {
	private final Language[] ourLanguages = { NeonLanguage.INSTANCE};

	@Override
	public Language[] getLanguages() {
		return ourLanguages;
	}

	@Override
	public boolean acceptElement(@NotNull PsiElement e) {
		return (e instanceof NeonKeyValPair)
				|| (e instanceof NeonKeyUsage)
				|| (e instanceof NeonParameterUsage)
				|| (e instanceof NeonClassReference && !(e.getParent() instanceof NeonKeyUsage) && !(e.getParent() instanceof NeonKey));
	}

	@Override
	public @Nullable Icon getElementIcon(@NotNull PsiElement e) {
		if (e instanceof NeonKeyUsage) {
			return NeonIcons.SERVICE;
		} else if (e instanceof NeonParameterUsage) {
			return NeonIcons.PARAMETER;
		} else if (e instanceof NeonClassReference) {
			return PhpIcons.CLASS;
		}
		return null;
	}

	@NotNull
	@Override
	public String getElementInfo(@NotNull PsiElement e) {
		if (e instanceof NeonKeyValPair) {
			return ((NeonKeyValPair) e).getKeyText();

		} else if (e instanceof NeonKeyUsage) {
			String text = ((NeonKeyUsage) e).getKeyText();
			return text.length() == 0 ? ((NeonKeyUsage) e).getClassName() : text;

		} else if (e instanceof NeonParameterUsage) {
			return ((NeonParameterUsage) e).getKeyText();

		} else if (e instanceof NeonClassReference) {
			return ((NeonClassReference) e).getClassName();

		} else {
			return "??";
		}
	}

	@Override
	public String getElementTooltip(@NotNull PsiElement e) {
		return e.toString();
	}

}
