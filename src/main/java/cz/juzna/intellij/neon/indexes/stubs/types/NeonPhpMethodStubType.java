package cz.juzna.intellij.neon.indexes.stubs.types;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.*;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.CharTable;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpMethodStub;
import cz.juzna.intellij.neon.indexes.extensions.NeonPhpMethodIndex;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpTypeStub;
import cz.juzna.intellij.neon.indexes.stubs.impl.NeonPhpMethodStubImpl;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonMethodUsage;
import cz.juzna.intellij.neon.psi.impl.NeonMethodUsageImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NeonPhpMethodStubType extends NeonPhpTypeStub<NeonPhpMethodStub, NeonMethodUsage> {
    public NeonPhpMethodStubType(String debugName) {
        super(debugName, NeonLanguage.INSTANCE);
    }

    @Override
        public NeonMethodUsage createPsi(@NotNull final NeonPhpMethodStub stub) {
        return new NeonMethodUsageImpl(stub, this);
    }

    @Override
    @NotNull
    public NeonPhpMethodStub createStub(@NotNull final NeonMethodUsage psi, final StubElement parentStub) {
        return new NeonPhpMethodStubImpl(parentStub, psi.getMethodName());
    }

    @Override
    @NotNull
    public String getExternalId() {
        return "neon.phpMethod.prop";
    }

    @Override
    public void serialize(@NotNull final NeonPhpMethodStub stub, @NotNull final StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getMethodName());
        //writePhpType(dataStream, stub.getPhpType());
    }

    @Override
    @NotNull
    public NeonPhpMethodStub deserialize(@NotNull final StubInputStream dataStream, final StubElement parentStub) throws IOException {
        String name = dataStream.readNameString();
        //NeonPhpType type = readPhpType(dataStream);
        return new NeonPhpMethodStubImpl(parentStub, name);
    }

    @Override
    public void indexStub(@NotNull final NeonPhpMethodStub stub, @NotNull final IndexSink sink) {
        sink.occurrence(NeonPhpMethodIndex.KEY, stub.getMethodName());
    }

    @NotNull
    @Override
    public NeonPhpMethodStub createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
        LighterASTNode keyNode = LightTreeUtil.firstChildOfType(tree, node, TokenSet.create(NeonTokenTypes.NEON_PHP_STATIC_METHOD, NeonTokenTypes.NEON_METHOD));
        String key = intern(tree.getCharTable(), keyNode);
        return new NeonPhpMethodStubImpl(parentStub, key);
    }

    public static String intern(@NotNull CharTable table, LighterASTNode node) {
        assert node instanceof LighterASTTokenNode : node;
        return table.intern(((LighterASTTokenNode) node).getText()).toString();
    }
}
