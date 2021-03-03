package cz.juzna.intellij.neon.indexes.stubs.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpClassStub;
import cz.juzna.intellij.neon.indexes.stubs.types.NeonPhpClassStubType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonClassReference;

public class NeonPhpClassStubImpl extends StubBase<NeonClassReference> implements NeonPhpClassStub {
    private final String className;

    public NeonPhpClassStubImpl(final StubElement parent, final String className) {
        super(parent, (NeonPhpClassStubType) NeonTokenTypes.CLASS_REFERENCE);
        this.className = className;
    }

    @Override
    public String getClassName() {
        return className;
    }
}
