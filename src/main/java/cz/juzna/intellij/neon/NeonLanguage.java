package cz.juzna.intellij.neon;


import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.psi.templateLanguages.TemplateLanguage;
import cz.juzna.intellij.neon.editor.NeonSyntaxHighlighter;


public class NeonLanguage extends Language implements TemplateLanguage {
	// singleton
	public static final NeonLanguage LANGUAGE = new NeonLanguage();

	public NeonLanguage() {
		super("Neon", "application/x-neon");

		// register highlighter - lazy singleton factory
		SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new SingleLazyInstanceSyntaxHighlighterFactory() {
			protected SyntaxHighlighter createHighlighter() {
				return new NeonSyntaxHighlighter();
			}
		});

	}
}
