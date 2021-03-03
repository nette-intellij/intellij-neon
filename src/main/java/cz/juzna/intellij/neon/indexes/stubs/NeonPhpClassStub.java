package cz.juzna.intellij.neon.indexes.stubs;

import com.intellij.psi.stubs.StubElement;
import cz.juzna.intellij.neon.psi.NeonClassReference;

public interface NeonPhpClassStub extends StubElement<NeonClassReference> {
    String getClassName();
}