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
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpConstantStub;
import cz.juzna.intellij.neon.indexes.extensions.NeonPhpConstantIndex;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpTypeStub;
import cz.juzna.intellij.neon.indexes.stubs.impl.NeonPhpConstantStubImpl;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonConstantUsage;
import cz.juzna.intellij.neon.psi.impl.NeonConstantUsageImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NeonPhpConstantStubType extends NeonPhpTypeStub<NeonPhpConstantStub, NeonConstantUsage> {
    public NeonPhpConstantStubType(String debugName) {
        super(debugName, NeonLanguage.INSTANCE);
    }

    @Override
    public NeonConstantUsage createPsi(@NotNull final NeonPhpConstantStub stub) {
        return new NeonConstantUsageImpl(stub, this);
    }

    @Override
    @NotNull
    public NeonPhpConstantStub createStub(@NotNull final NeonConstantUsage psi, final StubElement parentStub) {
        return new NeonPhpConstantStubImpl(parentStub, psi.getConstantName());
    }

    @Override
    @NotNull
    public String getExternalId() {
        return "Neon.phpConstant.prop";
    }

    @Override
    public void serialize(@NotNull final NeonPhpConstantStub stub, @NotNull final StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getConstantName());
    }

    @Override
    @NotNull
    public NeonPhpConstantStub deserialize(@NotNull final StubInputStream dataStream, final StubElement parentStub) throws IOException {
        String name = dataStream.readNameString();
        return new NeonPhpConstantStubImpl(parentStub, name);
    }

    @Override
    public void indexStub(@NotNull final NeonPhpConstantStub stub, @NotNull final IndexSink sink) {
        sink.occurrence(NeonPhpConstantIndex.KEY, stub.getConstantName());
    }

    @NotNull
    @Override
    public NeonPhpConstantStub createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
        LighterASTNode keyNode = LightTreeUtil.firstChildOfType(tree, node, TokenSet.create(NeonTokenTypes.NEON_PHP_STATIC_IDENTIFIER));
        String key = intern(tree.getCharTable(), keyNode);
        return new NeonPhpConstantStubImpl(parentStub, key);
    }

    public static String intern(@NotNull CharTable table, LighterASTNode node) {
        assert node instanceof LighterASTTokenNode : node;
        return table.intern(((LighterASTTokenNode) node).getText()).toString();
    }
}
