package cz.juzna.intellij.neon.psi.elements;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import cz.juzna.intellij.neon.config.NeonKeyChain;
import cz.juzna.intellij.neon.psi.NeonWholeString;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Key from key-value pair
 */
public interface NeonKeyElement extends NeonPsiElement, PsiNameIdentifierOwner {
	String getKeyText();

	boolean isServiceDefinition();

	boolean isParameterDefinition();

	@Nullable
	NeonWholeString getWholeString();

	NeonKeyChain getKeyChain(boolean isIncomplete);

	boolean isArrayBullet();

	List<PsiElement> getKeyTextElements();

}
