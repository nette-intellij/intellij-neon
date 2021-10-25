package cz.juzna.intellij.neon.editor;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.IndentOptions;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import cz.juzna.intellij.neon.NeonLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * Code style settings (tabs etc)
 */
public class NeonLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider
{

	@Override
	protected void customizeDefaults(@NotNull CommonCodeStyleSettings commonSettings, @NotNull IndentOptions indentOptions) {
		indentOptions.INDENT_SIZE = 4;
//		indentOptions.USE_TAB_CHARACTER = true;
		indentOptions.SMART_TABS = false;
		super.customizeDefaults(commonSettings, indentOptions);
	}

	public IndentOptionsEditor getIndentOptionsEditor()
	{
		return new IndentOptionsEditor();
	}

	@NotNull
	public Language getLanguage() {
		return NeonLanguage.INSTANCE;
	}

	public String getCodeSample(@NotNull SettingsType settingsType) {
		return "product:\n    name: Neon\n    version: 4\n    vendor: juzna.cz\n    url: \"http://blog.juzna.cz\"";
	}
}
