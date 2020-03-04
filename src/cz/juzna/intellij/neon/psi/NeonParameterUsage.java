package cz.juzna.intellij.neon.psi;

/**
 * Key from services eg: @foo
 */
public interface NeonParameterUsage extends NeonPsiElement {
	public String getKeyText();

}
