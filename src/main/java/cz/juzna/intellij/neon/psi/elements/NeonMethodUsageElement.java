package cz.juzna.intellij.neon.psi.elements;

import com.intellij.psi.StubBasedPsiElement;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpMethodStub;
import cz.juzna.intellij.neon.psi.NeonPhpElementUsage;

/**
 * Key from key-value pair
 */
public interface NeonMethodUsageElement extends NeonPsiElement, StubBasedPsiElement<NeonPhpMethodStub>, NeonPhpElementUsage {


}
