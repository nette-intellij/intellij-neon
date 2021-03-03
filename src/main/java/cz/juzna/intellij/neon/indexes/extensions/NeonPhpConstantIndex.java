package cz.juzna.intellij.neon.indexes.extensions;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import cz.juzna.intellij.neon.psi.NeonConstantUsage;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class NeonPhpConstantIndex extends StringStubIndexExtension<NeonConstantUsage> {
    public static final StubIndexKey<String, NeonConstantUsage> KEY = StubIndexKey.createIndexKey("neon.phpConstant.index");

    private static final NeonPhpConstantIndex ourInstance = new NeonPhpConstantIndex();

    public static NeonPhpConstantIndex getInstance() {
        return ourInstance;
    }

    @Override
    public int getVersion() {
        return super.getVersion() + 3;
    }

    @Override
    @NotNull
    public StubIndexKey<String, NeonConstantUsage> getKey() {
        return KEY;
    }

    @Override
    public Collection<NeonConstantUsage> get(@NotNull String key, @NotNull Project project, @NotNull GlobalSearchScope scope) {
        return StubIndex.getElements(getKey(), key, project, scope, NeonConstantUsage.class);
    }
}
