package cz.juzna.intellij.neon;


import com.intellij.lang.Language;
import com.intellij.psi.templateLanguages.TemplateLanguage;


public class NeonLanguage extends Language implements TemplateLanguage {
	// singleton
	public static final NeonLanguage LANGUAGE = new NeonLanguage();

	public NeonLanguage() {
		super("Neon", "application/x-neon");
	}
}
