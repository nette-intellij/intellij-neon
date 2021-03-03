package cz.juzna.intellij.neon.indexes.stubs.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpMethodStub;
import cz.juzna.intellij.neon.indexes.stubs.types.NeonPhpMethodStubType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonMethodUsage;

public class NeonPhpMethodStubImpl extends StubBase<NeonMethodUsage> implements NeonPhpMethodStub {
    private final String methodName;

    public NeonPhpMethodStubImpl(final StubElement parent, final String methodName) {
        super(parent, (NeonPhpMethodStubType) NeonTokenTypes.METHOD_USAGE);
        this.methodName = methodName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }
}
