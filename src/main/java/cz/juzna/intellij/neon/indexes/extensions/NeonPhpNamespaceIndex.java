package cz.juzna.intellij.neon.indexes.extensions;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import cz.juzna.intellij.neon.psi.NeonNamespaceReference;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class NeonPhpNamespaceIndex extends StringStubIndexExtension<NeonNamespaceReference> {
    public static final StubIndexKey<String, NeonNamespaceReference> KEY = StubIndexKey.createIndexKey("neon.phpNamespace.index");

    private static final NeonPhpNamespaceIndex ourInstance = new NeonPhpNamespaceIndex();

    public static NeonPhpNamespaceIndex getInstance() {
        return ourInstance;
    }

    @Override
    public int getVersion() {
        return super.getVersion() + 2;
    }

    @Override
    @NotNull
    public StubIndexKey<String, NeonNamespaceReference> getKey() {
        return KEY;
    }

    @Override
    public Collection<NeonNamespaceReference> get(@NotNull String key, @NotNull Project project, @NotNull GlobalSearchScope scope) {
        return StubIndex.getElements(getKey(), key, project, scope, NeonNamespaceReference.class);
    }
}
