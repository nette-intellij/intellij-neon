package cz.juzna.intellij.neon.psi;

/**
 * Key from services eg: @foo
 */
public interface NeonKeyUsage extends NeonPsiElement {
	public String getKeyText();

}
