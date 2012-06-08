package cz.juzna.intellij.neon;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.IndentOptions;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import org.jetbrains.annotations.NotNull;

/**
 * Code style settings (tabs etc)
 */
public class NeonLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider
{
	public CommonCodeStyleSettings getDefaultCommonSettings()
	{
		CommonCodeStyleSettings defaultSettings = new CommonCodeStyleSettings(NeonLanguage.LANGUAGE);
		CommonCodeStyleSettings.IndentOptions indentOptions = defaultSettings.initIndentOptions();
		indentOptions.INDENT_SIZE = 4;
//		indentOptions.USE_TAB_CHARACTER = true;
		indentOptions.SMART_TABS = false;
		
		return defaultSettings;
	}

//	public IndentOptionsEditor getIndentOptionsEditor()
//	{
//		return new NeonIndentOptionsEditor();
//	}

	@NotNull
	public Language getLanguage() {
		return NeonLanguage.LANGUAGE;
	}

	public String getCodeSample(@NotNull LanguageCodeStyleSettingsProvider.SettingsType settingsType) {
		return "product: \n  name: Neon\n  version: 4\n  vendor: juzna.cz\n  url: \"http://blog.juzna.cz\"";
	}

	public boolean usesSharedPreview() {
		return false;
	}

	/*
	private class NeonIndentOptionsEditor extends IndentOptionsEditor
	{
		private NeonIndentOptionsEditor()
		{
		}

		protected void addComponents()
		{
			addTabOptions();

			this.myCbUseTab.setEnabled(false);

			this.myTabSizeField = createIndentTextField();
			this.myTabSizeLabel = new JLabel(ApplicationBundle.message("editbox.indent.tab.size", new Object[0]));

			this.myIndentField = createIndentTextField();
			this.myIndentLabel = new JLabel(ApplicationBundle.message("editbox.indent.indent", new Object[0]));
			add(this.myIndentLabel, this.myIndentField);
		}

		public void setEnabled(boolean enabled)
		{
		}
	}*/
}
