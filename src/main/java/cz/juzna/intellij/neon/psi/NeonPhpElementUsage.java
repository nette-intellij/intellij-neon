package cz.juzna.intellij.neon.psi;

import cz.juzna.intellij.neon.util.NeonPhpType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Key from key-value pair
 */
public interface NeonPhpElementUsage {
    String getPhpElementName();

    @NotNull
    NeonPhpType getReturnType();

    @NotNull
    NeonPhpType getPhpType();

    @Nullable
    NeonPhpStatementElement getPhpStatement();

}
