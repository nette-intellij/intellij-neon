package cz.juzna.intellij.neon.indexes.stubs;

import com.intellij.psi.stubs.StubElement;
import cz.juzna.intellij.neon.psi.NeonConstantUsage;

public interface NeonPhpConstantStub extends StubElement<NeonConstantUsage> {
    String getConstantName();
}