package cz.juzna.intellij.neon.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.file.NeonFileType;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.elements.NeonPsiElement;
import org.jetbrains.annotations.NotNull;

public class NeonFileImpl extends PsiFileBase implements NeonFile {
	public NeonFileImpl(FileViewProvider viewProvider) {
		super(viewProvider, NeonLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return NeonFileType.INSTANCE;
	}

	@Override
	public String toString() {
		return "NeonFile:" + getName();
	}

	@Override
	public NeonPsiElement getValue() {
		for (PsiElement el : getChildren()) {
			if (el instanceof NeonPsiElement) {
				return (NeonPsiElement) el;
			}
		}
		return null;
	}

}
