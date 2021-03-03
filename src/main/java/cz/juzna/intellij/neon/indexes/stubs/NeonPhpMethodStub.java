package cz.juzna.intellij.neon.indexes.stubs;

import com.intellij.psi.stubs.StubElement;
import cz.juzna.intellij.neon.psi.NeonMethodUsage;

public interface NeonPhpMethodStub extends StubElement<NeonMethodUsage> {
    String getMethodName();
}