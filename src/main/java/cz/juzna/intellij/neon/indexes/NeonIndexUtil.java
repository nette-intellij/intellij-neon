package cz.juzna.intellij.neon.indexes;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import cz.juzna.intellij.neon.indexes.extensions.*;
import cz.juzna.intellij.neon.psi.*;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class NeonIndexUtil {
    public static Collection<NeonConstantUsage> findConstantsByName(@NotNull Project project, String name) {
        return NeonPhpConstantIndex.getInstance().get(name, project, GlobalSearchScope.allScope(project));
    }

    public static Collection<NeonStaticVariable> findStaticVariablesByName(@NotNull Project project, String name) {
        return NeonPhpStaticVariableIndex.getInstance().get(
                NeonPhpUtil.normalizePhpVariable(name),
                project,
                GlobalSearchScope.allScope(project)
        );
    }

    public static Collection<NeonNamespaceReference> findNamespacesByFqn(@NotNull Project project, String fqn) {
        return NeonPhpNamespaceIndex.getInstance().get(
                NeonPhpUtil.normalizeClassName(fqn),
                project,
                GlobalSearchScope.allScope(project)
        );
    }

    public static Collection<NeonMethodUsage> findMethodsByName(@NotNull Project project, String name) {
        return NeonPhpMethodIndex.getInstance().get(name, project, GlobalSearchScope.allScope(project));
    }

    public static Collection<NeonClassReference> getClassesByFqn(@NotNull Project project, String fqn) {
        return NeonPhpClassIndex.getInstance().get(
                NeonPhpUtil.normalizeClassName(fqn),
                project,
                GlobalSearchScope.allScope(project)
        );
    }

}