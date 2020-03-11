package cz.juzna.intellij.neon.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;

import java.util.ArrayList;
import java.util.List;

public class NeonParserUtil extends GeneratedParserUtilBase {

    static String myIndentString = "";
    static List<String> myIndentStrings = new ArrayList<String>();
    static int myIndent = 0;
    static int myPrevIndent = 0;

    public static void initialize() {
        myIndentString = "";
        myIndentStrings = new ArrayList<String>();
        myIndent = 0;
        myPrevIndent = 0;
    }

    public static boolean checkMainArray(PsiBuilder builder, int level) {
        if (builder.eof()) return false;

        PsiBuilder.Marker marker = builder.mark();
        boolean result = false;

        IElementType type = builder.getTokenType();
        if (type == NeonTokenTypes.NEON_INDENT && (builder.getTokenText() == null || builder.getTokenText().length() == 0)) {
            result = true;

        } else if (NeonTokenTypes.KEY_LITERALS.contains(type) || type == NeonTokenTypes.NEON_ARRAY_BULLET) {
            result = true;
        }

        marker.rollbackTo();

        return result;
    }

    public static boolean isSubArray(PsiBuilder builder, int level) {
        if (builder.eof()) return false;

        PsiBuilder.Marker marker = builder.mark();

        boolean result = true;

        IElementType type = builder.getTokenType();
        IElementType nextToken = builder.lookAhead(1);
        if (type == NeonTokenTypes.NEON_INDENT && nextToken == NeonTokenTypes.NEON_INDENT) {
            result = false;
        }

        marker.rollbackTo();

        return result;
    }

    public static boolean isNamespace(PsiBuilder builder, int level) {
        PsiBuilder.Marker marker = builder.mark();

        boolean result = false;

        IElementType type = builder.getTokenType();
        IElementType nextToken = builder.lookAhead(1);
        if (type == NeonTokenTypes.NEON_NAMESPACE_REFERENCE && nextToken == NeonTokenTypes.NEON_NAMESPACE_RESOLUTION) {
            result = true;
        }

        marker.rollbackTo();

        return result;
    }

    public static boolean checkSubArray(PsiBuilder builder, int level) {
        if (builder.eof()) return false;

        PsiBuilder.Marker marker = builder.mark();
        boolean result = false;

        IElementType type = builder.getTokenType();
        if (
                type == NeonTokenTypes.NEON_INDENT
                && builder.getTokenText() != null
        ) {
            if (builder.getTokenText().replace("\n", "").length() > 0) {
                String indent = builder.getTokenText().replace("\n", "");
                int len = indent.length();
                int step = Math.min(myIndentStrings.size() > 0 ? myIndentStrings.get(0).length() : 0, len);

                result = myPrevIndent <= len;

                if (step < Math.max(myPrevIndent - len, 0)) {
                    myPrevIndent = myPrevIndent - step;
                } else {
                    myPrevIndent = len;
                }

            } else {
                myPrevIndent = 0;
            }

        }

        marker.rollbackTo();

        return result;
    }

    public static boolean useArray(PsiBuilder builder, int level) {
        if (!isIndentsValid()) {
            error("bad indent");
            return false;
        }
        return true;
    }

    public static boolean checkValidIndent(PsiBuilder builder, int level) {
        if (builder.eof()) return false;

        PsiBuilder.Marker marker = builder.mark();
        boolean result = true;

        do {
            IElementType type = builder.getTokenType();
            if (type == NeonTokenTypes.NEON_INDENT) {
                result = validateTabsSpaces(builder);
                myIndent = builder.getTokenText().length();
                if (builder.getTokenText().charAt(0) == '\n') {
                    myIndent--;
                }
            }

            builder.advanceLexer();
        }
        while (result && builder.getTokenType() == NeonTokenTypes.NEON_INDENT && builder.lookAhead(1) == NeonTokenTypes.NEON_INDENT); // keep going if we're still indented

        marker.rollbackTo();

        return result;
    }

    private static boolean validateTabsSpaces(PsiBuilder builder) {
        assert builder.getTokenType() == NeonTokenTypes.NEON_INDENT;
        String text = builder.getTokenText().replace("\n", "");
        if (text.isEmpty() && builder.lookAhead(1) == NeonTokenTypes.NEON_INDENT) {
            return true;
        }
        if (text.isEmpty()) {
            myIndentStrings = new ArrayList<String>();
            myIndentString = "";
            return true;
        }

        int min = Math.min(myIndentString.length(), text.length());
        if (myIndentString.length() > 0 && !text.substring(0, min).equals(myIndentString.substring(0, min))) {
            error("Tab/space mixing");
            return false;
        } else {
            String indent = determineCurrentIndent(text);
            if (indent.length() > 0) {
                myIndentStrings.add(determineCurrentIndent(text));
            }
            myIndentString = text;
        }
        return true;
    }

    private static String determineCurrentIndent(String text) {
        for (String indent : myIndentStrings) {
            if (text.length() >= indent.length()) {
                text = text.substring(indent.length());
            }
        }
        return text;
    }

    private static boolean isIndentsValid() {
        int length = 0;
        for (String indent : myIndentStrings) {
            if (length == 0) {
                length = indent.length();
            }

            if (indent.length() != length) {
                return false;
            }
        }
        return true;
    }

}
