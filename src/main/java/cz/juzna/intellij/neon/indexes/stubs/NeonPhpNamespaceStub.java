package cz.juzna.intellij.neon.indexes.stubs;

import com.intellij.psi.stubs.StubElement;
import cz.juzna.intellij.neon.psi.NeonNamespaceReference;

public interface NeonPhpNamespaceStub extends StubElement<NeonNamespaceReference> {
    String getNamespaceName();
}