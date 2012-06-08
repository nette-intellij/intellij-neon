package cz.juzna.intellij.neon.editor;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import cz.juzna.intellij.neon.NeonLanguage;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NeonCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
	@NotNull
	@Override
	public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSetting) {
		return new CodeStyleAbstractConfigurable(settings, originalSetting, getConfigurableDisplayName()) {
			@Override
			protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
				return new TabbedLanguageCodeStylePanel(NeonLanguage.LANGUAGE, getCurrentSettings(), settings) {
					@Override
					protected void initTabs(CodeStyleSettings settings) {
						addIndentOptionsTab(settings);
					}
				};
			}

			@Override
			public String getHelpTopic() {
				return "reference.settingsdialog.codestyle.neon"; // what is this?
			}
		};
	}

	@Override
	public String getConfigurableDisplayName() {
		return NeonLanguage.LANGUAGE.getDisplayName();
	}
}
