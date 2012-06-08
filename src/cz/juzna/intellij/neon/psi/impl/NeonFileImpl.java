package cz.juzna.intellij.neon.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.file.NeonFileType;
import cz.juzna.intellij.neon.psi.NeonFile;
import org.jetbrains.annotations.NotNull;

public class NeonFileImpl extends PsiFileBase implements NeonFile {
    public NeonFileImpl(FileViewProvider viewProvider) {
        super(viewProvider, NeonLanguage.LANGUAGE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return NeonFileType.NEON_FILE_TYPE;
    }

    @Override
    public String toString() {
        return "NeonFile:" + getName();
    }
}
