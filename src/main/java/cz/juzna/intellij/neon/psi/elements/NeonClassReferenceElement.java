package cz.juzna.intellij.neon.psi.elements;

import com.intellij.psi.StubBasedPsiElement;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpClassStub;

/**
 * Key from key-value pair
 */
public interface NeonClassReferenceElement extends NeonPsiElement, StubBasedPsiElement<NeonPhpClassStub> {


}
