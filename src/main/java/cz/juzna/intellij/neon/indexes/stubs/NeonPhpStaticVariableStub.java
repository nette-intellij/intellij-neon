package cz.juzna.intellij.neon.indexes.stubs;

import com.intellij.psi.stubs.StubElement;
import cz.juzna.intellij.neon.psi.NeonStaticVariable;

public interface NeonPhpStaticVariableStub extends StubElement<NeonStaticVariable> {
    String getVariableName();
}