package cz.juzna.intellij.neon.reference;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.lexer.NeonHighlightingLexer;
import cz.juzna.intellij.neon.lexer.NeonLexer;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NeonFindUsagesProvider implements FindUsagesProvider {
	@Nullable
	@Override
	public WordsScanner getWordsScanner() {
		return new DefaultWordsScanner(
				new NeonHighlightingLexer(new NeonLexer()),
				TokenSet.create(NeonTokenTypes.NEON_KEY),
				TokenSet.create(NeonTokenTypes.NEON_COMMENT),
				TokenSet.EMPTY
		);
	}

	@Override
	public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
		return psiElement instanceof NeonKey;
	}

	@Nullable
	@Override
	public String getHelpId(@NotNull PsiElement psiElement) {
		return null;
	}

	@NotNull
	@Override
	public String getType(@NotNull PsiElement element) {
		if (element instanceof NeonKey) {
			return "neon key";
		} else {
			return "";
		}
	}

	@NotNull
	@Override
	public String getDescriptiveName(@NotNull PsiElement element) {
		if (element instanceof NeonKey) {
			return ((NeonKey) element).getKeyText();
		} else {
			return "";
		}
	}

	@NotNull
	@Override
	public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
		if (element instanceof NeonKey) {
			return ((NeonKey) element).getKeyText();
		} else {
			return "";
		}
	}
}
