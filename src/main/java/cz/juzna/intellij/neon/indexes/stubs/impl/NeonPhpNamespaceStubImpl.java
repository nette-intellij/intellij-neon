package cz.juzna.intellij.neon.indexes.stubs.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpNamespaceStub;
import cz.juzna.intellij.neon.indexes.stubs.types.NeonPhpNamespaceStubType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonNamespaceReference;

public class NeonPhpNamespaceStubImpl extends StubBase<NeonNamespaceReference> implements NeonPhpNamespaceStub {
    private final String namespaceName;

    public NeonPhpNamespaceStubImpl(final StubElement parent, final String constantName) {
        super(parent, (NeonPhpNamespaceStubType) NeonTokenTypes.NAMESPACE_REFERENCE);
        this.namespaceName = constantName;
    }

    @Override
    public String getNamespaceName() {
        return namespaceName;
    }
}