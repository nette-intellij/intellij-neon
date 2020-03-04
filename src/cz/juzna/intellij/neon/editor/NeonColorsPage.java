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
            new AttributesDescriptor("colors.comment", NeonSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("colors.identifier", NeonSyntaxHighlighter.IDENTIFIER),
            new AttributesDescriptor("colors.interpunction", NeonSyntaxHighlighter.INTERPUNCTION),
            new AttributesDescriptor("colors.number", NeonSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("colors.keyword", NeonSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("colors.string", NeonSyntaxHighlighter.STRING),
            new AttributesDescriptor("colors.key.usage", NeonSyntaxHighlighter.KEY_USAGE),
            new AttributesDescriptor("colors.parameter.usage", NeonSyntaxHighlighter.PARAMETER_USAGE),
            new AttributesDescriptor("colors.method", NeonSyntaxHighlighter.METHOD),
            new AttributesDescriptor("colors.php.identifier", NeonSyntaxHighlighter.PHP_IDENTIFIER),
            new AttributesDescriptor("colors.php.staticIdentifier", NeonSyntaxHighlighter.PHP_STATIC_IDENTIFIER),
            new AttributesDescriptor("colors.className", NeonSyntaxHighlighter.CLASS_NAME),
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
        return "common:\n" +
                "\tparameters:\n" +
                "\t\tdays: [ Mon, Tue, Wed ]\n" +
                "\t\tdays2:\n" +
                "\t\t\t- Mon # Monday\n" +
                "\t\t\t- Tue\n" +
                "\t\t\t- Wed\n" +
                "\n" +
                "\t\t# Third type\n" +
                "\t\tdayNames: { mon: Monday, tue: Tuesday }\n" +
                "\n" +
                "\t\taddress:\n" +
                "\t\t\tstreet: 742 Evergreen Terrace\n" +
                "\t\t\tcity: Springfield\n" +
                "\t\t\tcountry: USA\n" +
                "\n" +
                "\t\tphones: { home: 555-6528, work: 555-7334 }\n" +
                "\t\tphp:\n" +
                "\t\t\tdate.timezone: Europe/Prague\n" +
                "\t\t\tzlib.output_compression: yes  # use gzip\n" +
                "\n" +
                "\t\tchildren:\n" +
                "\t\t\t- Bart\n" +
                "\t\t\t- Lisa\n" +
                "\t\t\t- %children.third%\n" +
                "\n" +
                "\t\tentity: Column(type=\"integer\")\n" +
                "\n" +
                "\tservices:\n" +
                "\t\tmyService: Nette\\Object(\"AHOJ\")\n" +
                "\t\tmyService2: Nette\\Something(@myService, 1)\n" +
                "\n" +
                "\n" +
                "production < common:\n" +
                "\tservices:\n" +
                "\t\tauthenticator: Nette\\Authenticator(@db)\n" +
                "\n" +
                "development < common:\n";
    }

    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }
}
