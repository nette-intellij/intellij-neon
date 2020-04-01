package cz.juzna.intellij.neon.psi;

import cz.juzna.intellij.neon.util.NeonPhpType;
import org.jetbrains.annotations.NotNull;

/**
 * Key from key-value pair
 */
public interface NeonPhpElementUsage {
    String getPhpElementName();

    @NotNull
    NeonPhpType getPhpType();

}
