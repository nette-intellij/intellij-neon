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
import cz.juzna.intellij.neon.indexes.extensions.NeonPhpClassIndex;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpClassStub;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpTypeStub;
import cz.juzna.intellij.neon.indexes.stubs.impl.NeonPhpClassStubImpl;
import cz.juzna.intellij.neon.psi.NeonClassReference;
import cz.juzna.intellij.neon.psi.impl.NeonClassReferenceImpl;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static cz.juzna.intellij.neon.lexer.NeonTokenTypes.*;

public class NeonPhpClassStubType extends NeonPhpTypeStub<NeonPhpClassStub, NeonClassReference> {
    private static final TokenSet classElements = TokenSet.create(NEON_NAMESPACE_REFERENCE, NEON_NAMESPACE_RESOLUTION, NEON_IDENTIFIER);

    public NeonPhpClassStubType(String debugName) {
        super(debugName, NeonLanguage.INSTANCE);
    }

    @Override
    public NeonClassReference createPsi(@NotNull final NeonPhpClassStub stub) {
        return new NeonClassReferenceImpl(stub, this);
    }

    @Override
    @NotNull
    public NeonPhpClassStub createStub(@NotNull final NeonClassReference psi, final StubElement parentStub) {
        return new NeonPhpClassStubImpl(parentStub, psi.getClassName());
    }

    @Override
    @NotNull
    public String getExternalId() {
        return "neon.phpClass.name";
    }

    @Override
    public void serialize(@NotNull final NeonPhpClassStub stub, @NotNull final StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getClassName());
    }

    @Override
    @NotNull
    public NeonPhpClassStub deserialize(@NotNull final StubInputStream dataStream, final StubElement parentStub) throws IOException {
        String name = dataStream.readNameString();
        return new NeonPhpClassStubImpl(parentStub, name);
    }

    @Override
    public void indexStub(@NotNull final NeonPhpClassStub stub, @NotNull final IndexSink sink) {
        sink.occurrence(NeonPhpClassIndex.KEY, stub.getClassName());
    }

    @NotNull
    @Override
    public NeonPhpClassStub createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
        List<LighterASTNode> keyNode = LightTreeUtil.getChildrenOfType(tree, node, TokenSet.create(NEON_NAMESPACE_REFERENCE, NEON_NAMESPACE_RESOLUTION, CLASS_USAGE));
        StringBuilder builder = new StringBuilder();
        for (LighterASTNode astNode : keyNode) {
            if (classElements.contains(astNode.getTokenType())) {
                builder.append(intern(tree.getCharTable(), astNode));
                continue;
            }

            List<LighterASTNode> children = LightTreeUtil.getChildrenOfType(tree, astNode, classElements);
            for (LighterASTNode current : children) {
                builder.append(intern(tree.getCharTable(), current));
            }
        }
        return new NeonPhpClassStubImpl(parentStub, NeonPhpUtil.normalizeClassName(builder.toString()));
    }

    public static String intern(@NotNull CharTable table, LighterASTNode node) {
        assert node instanceof LighterASTTokenNode : node;
        return table.intern(((LighterASTTokenNode) node).getText()).toString();
    }
}
