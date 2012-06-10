package cz.juzna.intellij.neon.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.file.NeonFileType;
import cz.juzna.intellij.neon.lexer.NeonTokenType;
import cz.juzna.intellij.neon.parser.NeonElementTypes;
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.NeonSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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

	public HashMap<String, NeonSection> getSections() {
		HashMap<String, NeonSection> ret = new HashMap<String, NeonSection>();

		for (ASTNode node : getNode().getChildren(TokenSet.create(NeonElementTypes.KEY_VALUE_PAIR))) {
			NeonSection section = (NeonSection) node.getPsi();
			ret.put(section.getKeyText(), section);
		}

		return ret;
	}

}
