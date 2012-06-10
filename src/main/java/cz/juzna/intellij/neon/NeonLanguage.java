package cz.juzna.intellij.neon;


import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.psi.templateLanguages.TemplateLanguage;
import cz.juzna.intellij.neon.editor.NeonSyntaxHighlighter;


public class NeonLanguage extends Language {
	// singleton
	public static final NeonLanguage LANGUAGE = new NeonLanguage();

	public NeonLanguage() {
		super("neon", "application/x-neon");
	}

	@Override
	public String getDisplayName() {
		return "Neon";
	}
}
