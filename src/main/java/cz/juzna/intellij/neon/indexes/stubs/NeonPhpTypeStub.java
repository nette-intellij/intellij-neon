package cz.juzna.intellij.neon.indexes.stubs;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract public class NeonPhpTypeStub<S extends StubElement<?>, P extends PsiElement> extends ILightStubElementType<S, P> {
    public NeonPhpTypeStub(@NotNull String debugName, @Nullable Language language) {
        super(debugName, language);
    }

}
