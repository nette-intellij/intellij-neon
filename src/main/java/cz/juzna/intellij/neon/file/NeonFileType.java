package cz.juzna.intellij.neon.file;

import com.intellij.openapi.fileTypes.LanguageFileType;
import cz.juzna.intellij.neon.Neon;
import cz.juzna.intellij.neon.NeonIcons;
import cz.juzna.intellij.neon.NeonLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class NeonFileType extends LanguageFileType {
	public static final NeonFileType INSTANCE = new NeonFileType();
	public static final String DEFAULT_EXTENSION = "neon";

	/**
	 * All extensions which are associated with this plugin.
	 */
	public static final String[] extensions = {
		DEFAULT_EXTENSION
	};


	protected NeonFileType() {
		super(NeonLanguage.INSTANCE);
	}

	@NotNull
	public String getName() {
		return Neon.LANGUAGE_NAME;
	}

	@NotNull
	public String getDescription() {
		return Neon.LANGUAGE_DESCRIPTION;
	}

	@NotNull
	public String getDefaultExtension() {
		return DEFAULT_EXTENSION;
	}

	@NotNull
	public Icon getIcon() {
		return NeonIcons.FILETYPE_ICON;
	}
}

