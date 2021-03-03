package cz.juzna.intellij.neon.indexes.extensions;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import cz.juzna.intellij.neon.psi.NeonStaticVariable;
import cz.juzna.intellij.neon.psi.NeonStaticVariable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class NeonPhpStaticVariableIndex extends StringStubIndexExtension<NeonStaticVariable> {
    public static final StubIndexKey<String, NeonStaticVariable> KEY = StubIndexKey.createIndexKey("neon.phpStaticVariable.index");

    private static final NeonPhpStaticVariableIndex ourInstance = new NeonPhpStaticVariableIndex();

    public static NeonPhpStaticVariableIndex getInstance() {
        return ourInstance;
    }

    @Override
    public int getVersion() {
        return super.getVersion() + 1;
    }

    @Override
    @NotNull
    public StubIndexKey<String, NeonStaticVariable> getKey() {
        return KEY;
    }

    @Override
    public Collection<NeonStaticVariable> get(@NotNull String key, @NotNull Project project, @NotNull GlobalSearchScope scope) {
        return StubIndex.getElements(getKey(), key, project, scope, NeonStaticVariable.class);
    }
}
