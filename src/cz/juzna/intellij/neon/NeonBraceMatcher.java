package cz.juzna.intellij.neon;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Matches starting-closing braces in neon
 */
public class NeonBraceMatcher implements PairedBraceMatcher, NeonTokenTypes {
	private static final BracePair[] PAIRS = {
// FIXME: need to patch lexer to output braces
//		new BracePair(LBRACE, RBRACE, true),
//		new BracePair(LBRACKET, RBRACKET, true)
	};

	@Override
	public BracePair[] getPairs() {
		return PAIRS;
	}

	@Override
	public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType iElementType, @Nullable IElementType iElementType1) {
		return true;
	}

	@Override
	public int getCodeConstructStart(PsiFile psiFile, int openingBraceOffset) {
		return openingBraceOffset;
	}
}
