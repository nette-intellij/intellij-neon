package cz.juzna.intellij.neon.indexes.stubs.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpStaticVariableStub;
import cz.juzna.intellij.neon.indexes.stubs.types.NeonPhpStaticVariableStubType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonStaticVariable;

public class NeonPhpStaticVariableStubImpl extends StubBase<NeonStaticVariable> implements NeonPhpStaticVariableStub {
    private final String variableName;

    public NeonPhpStaticVariableStubImpl(final StubElement parent, final String variableName) {
        super(parent, (NeonPhpStaticVariableStubType) NeonTokenTypes.STATIC_VARIABLE);
        this.variableName = variableName;
    }

    @Override
    public String getVariableName() {
        return variableName;
    }
}
