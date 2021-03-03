package cz.juzna.intellij.neon.indexes.extensions;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import cz.juzna.intellij.neon.psi.NeonClassReference;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class NeonPhpClassIndex extends StringStubIndexExtension<NeonClassReference> {
    public static final StubIndexKey<String, NeonClassReference> KEY = StubIndexKey.createIndexKey("neon.phpClass.index");

    private static final NeonPhpClassIndex ourInstance = new NeonPhpClassIndex();

    public static NeonPhpClassIndex getInstance() {
        return ourInstance;
    }

    @Override
    public int getVersion() {
        return super.getVersion() + 1;
    }

    @Override
    @NotNull
    public StubIndexKey<String, NeonClassReference> getKey() {
        return KEY;
    }

    @Override
    public Collection<NeonClassReference> get(@NotNull String key, @NotNull Project project, @NotNull GlobalSearchScope scope) {
        return StubIndex.getElements(getKey(), key, project, scope, NeonClassReference.class);
    }
}
