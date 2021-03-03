package cz.juzna.intellij.neon.psi.elements;

import com.intellij.psi.StubBasedPsiElement;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpStaticVariableStub;
import cz.juzna.intellij.neon.psi.NeonPhpElementUsage;

/**
 * Key from key-value pair
 */
public interface NeonStaticVariableElement extends NeonPsiElement, StubBasedPsiElement<NeonPhpStaticVariableStub>, NeonPhpElementUsage {


}
