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
import cz.juzna.intellij.neon.indexes.extensions.NeonPhpStaticVariableIndex;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpStaticVariableStub;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpTypeStub;
import cz.juzna.intellij.neon.indexes.stubs.impl.NeonPhpStaticVariableStubImpl;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.NeonStaticVariable;
import cz.juzna.intellij.neon.psi.impl.NeonStaticVariableImpl;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NeonPhpStaticVariableStubType extends NeonPhpTypeStub<NeonPhpStaticVariableStub, NeonStaticVariable> {
    public NeonPhpStaticVariableStubType(String debugName) {
        super(debugName, NeonLanguage.INSTANCE);
    }

    @Override
    public NeonStaticVariable createPsi(@NotNull final NeonPhpStaticVariableStub stub) {
        return new NeonStaticVariableImpl(stub, this);
    }

    @Override
    @NotNull
    public NeonPhpStaticVariableStub createStub(@NotNull final NeonStaticVariable psi, final StubElement parentStub) {
        return new NeonPhpStaticVariableStubImpl(parentStub, psi.getVariableName());
    }

    @Override
    @NotNull
    public String getExternalId() {
        return "neon.phpStaticVariable.prop";
    }

    @Override
    public void serialize(@NotNull final NeonPhpStaticVariableStub stub, @NotNull final StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getVariableName());
    }

    @Override
    @NotNull
    public NeonPhpStaticVariableStub deserialize(@NotNull final StubInputStream dataStream, final StubElement parentStub) throws IOException {
        String name = dataStream.readNameString();
        return new NeonPhpStaticVariableStubImpl(parentStub, name);
    }

    @Override
    public void indexStub(@NotNull final NeonPhpStaticVariableStub stub, @NotNull final IndexSink sink) {
        sink.occurrence(NeonPhpStaticVariableIndex.KEY, stub.getVariableName());
    }

    @NotNull
    @Override
    public NeonPhpStaticVariableStub createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
        LighterASTNode keyNode = LightTreeUtil.firstChildOfType(tree, node, TokenSet.create(NeonTokenTypes.NEON_PHP_VARIABLE_USAGE));
        String key = intern(tree.getCharTable(), keyNode);
        return new NeonPhpStaticVariableStubImpl(parentStub, NeonPhpUtil.normalizePhpVariable(key));
    }

    public static String intern(@NotNull CharTable table, LighterASTNode node) {
        assert node instanceof LighterASTTokenNode : node;
        return table.intern(((LighterASTTokenNode) node).getText()).toString();
    }
}