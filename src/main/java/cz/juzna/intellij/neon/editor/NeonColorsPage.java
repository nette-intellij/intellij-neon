package cz.juzna.intellij.neon.editor;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Map;


/**
 * Settings dialog for colors
 */
public class NeonColorsPage implements ColorSettingsPage {
	public static final AttributesDescriptor[] ATTRS = {
			new AttributesDescriptor("colors.bad.character", NeonSyntaxHighlighter.UNKNOWN),
			new AttributesDescriptor("colors.comment",       NeonSyntaxHighlighter.COMMENT),
			new AttributesDescriptor("colors.block",         NeonSyntaxHighlighter.BLOCK),
			new AttributesDescriptor("colors.interpunction", NeonSyntaxHighlighter.INTERPUNCTION),
			new AttributesDescriptor("colors.number",        NeonSyntaxHighlighter.NUMBER),
			new AttributesDescriptor("colors.keyword",       NeonSyntaxHighlighter.KEYWORD),
	};

	@NotNull
	@Override
	public String getDisplayName() {
		return "Neon";
	}

	@Override
	public Icon getIcon() {
		return null;
	}

	@NotNull
	@Override
	public AttributesDescriptor[] getAttributeDescriptors() {
		return ATTRS;
	}

	@NotNull
	@Override
	public ColorDescriptor[] getColorDescriptors() {
		return new ColorDescriptor[0];
	}

	@NotNull
	@Override
	public SyntaxHighlighter getHighlighter() {
		return new NeonSyntaxHighlighter();
	}

	@NotNull
	@Override
	public String getDemoText() {
		return "pepa: novak\n";
	}

	@Override
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
		return null;
	}
}
