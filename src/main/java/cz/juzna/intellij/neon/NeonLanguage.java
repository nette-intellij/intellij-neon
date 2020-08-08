package cz.juzna.intellij.neon;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

public class NeonLanguage extends Language {
	// singleton
	public static final NeonLanguage INSTANCE = new NeonLanguage();
	public static final String MIME_TYPE = "text/x-neon";

	public NeonLanguage() {
		super("neon", MIME_TYPE);
	}

	@NotNull
	@Override
	public String getDisplayName() {
		return "Neon";
	}
}
