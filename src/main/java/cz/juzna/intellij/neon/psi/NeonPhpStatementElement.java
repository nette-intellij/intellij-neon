package cz.juzna.intellij.neon.psi;

import cz.juzna.intellij.neon.psi.elements.NeonPsiElement;
import cz.juzna.intellij.neon.util.NeonPhpType;
import org.jetbrains.annotations.NotNull;

public interface NeonPhpStatementElement extends NeonPsiElement {
    @NotNull
    NeonPhpType getPhpType();

    boolean isStatic();

}
