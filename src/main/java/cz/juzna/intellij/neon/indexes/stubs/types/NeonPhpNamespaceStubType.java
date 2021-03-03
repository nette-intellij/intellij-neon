package cz.juzna.intellij.neon.indexes.stubs.types;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.CharTable;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.indexes.extensions.NeonPhpNamespaceIndex;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpNamespaceStub;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpTypeStub;
import cz.juzna.intellij.neon.indexes.stubs.impl.NeonPhpNamespaceStubImpl;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonNamespaceReference;
import cz.juzna.intellij.neon.psi.impl.NeonNamespaceReferenceImpl;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NeonPhpNamespaceStubType extends NeonPhpTypeStub<NeonPhpNamespaceStub, NeonNamespaceReference> {
    public NeonPhpNamespaceStubType(String debugName) {
        super(debugName, NeonLanguage.INSTANCE);
    }

    @Override
    public NeonNamespaceReference createPsi(@NotNull final NeonPhpNamespaceStub stub) {
        return new NeonNamespaceReferenceImpl(stub, this);
    }

    @Override
    @NotNull
    public NeonPhpNamespaceStub createStub(@NotNull final NeonNamespaceReference psi, final StubElement parentStub) {
        return new NeonPhpNamespaceStubImpl(parentStub, psi.getNamespaceName());
    }

    @Override
    @NotNull
    public String getExternalId() {
        return "neon.phpNamespace.prop";
    }

    @Override
    public void serialize(@NotNull final NeonPhpNamespaceStub stub, @NotNull final StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getNamespaceName());
    }

    @Override
    @NotNull
    public NeonPhpNamespaceStub deserialize(@NotNull final StubInputStream dataStream, final StubElement parentStub) throws IOException {
        String name = dataStream.readNameString();
        return new NeonPhpNamespaceStubImpl(parentStub, name);
    }

    @Override
    public void indexStub(@NotNull final NeonPhpNamespaceStub stub, @NotNull final IndexSink sink) {
        sink.occurrence(NeonPhpNamespaceIndex.KEY, stub.getNamespaceName());
    }

    @NotNull
    @Override
    public NeonPhpNamespaceStub createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
        LighterASTNode keyNode = LightTreeUtil.firstChildOfType(tree, node, TokenSet.create(NeonTokenTypes.NEON_NAMESPACE_REFERENCE));
        String key = intern(tree.getCharTable(), keyNode);
        return new NeonPhpNamespaceStubImpl(parentStub, NeonPhpUtil.normalizeClassName(key));
    }

    public static String intern(@NotNull CharTable table, LighterASTNode node) {
        assert node instanceof LighterASTTokenNode : node;
        return table.intern(((LighterASTTokenNode) node).getText()).toString();
    }
}
