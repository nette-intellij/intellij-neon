package cz.juzna.intellij.neon.indexes.stubs.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpConstantStub;
import cz.juzna.intellij.neon.indexes.stubs.types.NeonPhpConstantStubType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonConstantUsage;

public class NeonPhpConstantStubImpl extends StubBase<NeonConstantUsage> implements NeonPhpConstantStub {
    private final String constantName;

    public NeonPhpConstantStubImpl(final StubElement parent, final String constantName) {
        super(parent, (NeonPhpConstantStubType) NeonTokenTypes.CONSTANT_USAGE);
        this.constantName = constantName;
    }

    @Override
    public String getConstantName() {
        return constantName;
    }
}
