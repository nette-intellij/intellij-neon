package cz.juzna.intellij.neon.indexes.extensions;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import cz.juzna.intellij.neon.psi.NeonMethodUsage;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class NeonPhpMethodIndex extends StringStubIndexExtension<NeonMethodUsage> {
    public static final StubIndexKey<String, NeonMethodUsage> KEY = StubIndexKey.createIndexKey("neon.phpMethod.index");

    private static final NeonPhpMethodIndex ourInstance = new NeonPhpMethodIndex();

    public static NeonPhpMethodIndex getInstance() {
        return ourInstance;
    }

    @Override
    public int getVersion() {
        return super.getVersion() + 3;
    }

    @Override
    @NotNull
    public StubIndexKey<String, NeonMethodUsage> getKey() {
        return KEY;
    }

    @Override
    public Collection<NeonMethodUsage> get(@NotNull String key, @NotNull Project project, @NotNull GlobalSearchScope scope) {
        return StubIndex.getElements(getKey(), key, project, scope, NeonMethodUsage.class);
    }
}
