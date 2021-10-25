package cz.juzna.intellij.neon.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.tree.IElementType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeonParserUtil extends GeneratedParserUtilBase {

    public static String NOTIFICATION_GROUP = "Neon";

    static String myIndentString = "";
    static List<String> myIndentStrings = new ArrayList<>();
    static int myIndent = 0;
    static int myPrevIndent = 0;
    static int depth = 0;

    public static void initialize() {
        myIndentString = "";
        myIndentStrings = new ArrayList<>();
        myIndent = 0;
        myPrevIndent = 0;
        depth = 0;
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

    public static boolean increaseDepth(PsiBuilder builder, int level) {
        if (builder.eof()) {
            return false;
        }
        depth++;
        return true;
    }

    public static boolean decreaseDepth(PsiBuilder builder, int level) {
        if (builder.eof()) {
            return false;
        }
        depth--;
        return true;
    }

    public static boolean isSubArray(PsiBuilder builder, int level) {
        if (builder.eof()) {
            return false;
        }

        PsiBuilder.Marker marker = builder.mark();

        boolean result = true;

        IElementType type = builder.getTokenType();
        IElementType nextToken = builder.lookAhead(1);
        if (type == NeonTokenTypes.NEON_INDENT && (nextToken == NeonTokenTypes.NEON_INDENT || nextToken == null)) {
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
        if (builder.eof()) {
            return false;
        }

        PsiBuilder.Marker marker = builder.mark();
        boolean result = false;

        String tokenText = builder.getTokenText();
        IElementType type = builder.getTokenType();
        if (
                type == NeonTokenTypes.NEON_INDENT
                && builder.getTokenText() != null
        ) {
            if (builder.getTokenText().replace("\n", "").length() > 0) {
                String indent = builder.getTokenText().replace("\n", "");
                int len = indent.length();
                int step = Math.min(myIndentStrings.size() > 0 ? myIndentStrings.get(0).length() : 0, len);

                IElementType nextToken = builder.lookAhead(1);
                //if (nextToken == NeonTokenTypes.NEON_KEY)

                //result = myPrevIndent <= len;
                result = nextToken == NeonTokenTypes.NEON_KEY;


               /* int remaining = len;
                List<String> reversed = new ArrayList<>(myIndentStrings);
                Collections.reverse(reversed);
                for (String currentIndent : myIndentStrings) {
                    remaining -= currentIndent.length();
                    //todo
                }*/

                /*if (step < Math.max(myPrevIndent - len, 0)) {
                    myPrevIndent = myPrevIndent - step;
                } else {
                    myPrevIndent = len;
                }*/
                myPrevIndent = len;

            } else {
                myPrevIndent = 0;
            }

        }

        marker.rollbackTo();

        return result;
    }

    public static boolean useArray(PsiBuilder builder, int level) {
//        if (!isIndentsValid()) {
//            error("bad indent");
//            return false;
//        }
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
                String tokenText = builder.getTokenText();
                myIndent = tokenText != null ? tokenText.length() : 0;
                if (tokenText != null && tokenText.charAt(0) == '\n') {
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
        String text = builder.getTokenText() != null ? builder.getTokenText().replace("\n", "") : "";
        if (text.isEmpty() && builder.lookAhead(1) == NeonTokenTypes.NEON_INDENT) {
            return true;
        }
        if (text.isEmpty()) {
            myIndentStrings = new ArrayList<>();
            myIndentString = "";
            return true;
        }

        int textLength = text.length();
        int indentLength = myIndentString.length();
        try {
            int endIndex = Math.max(0, Math.min(indentLength - 1, textLength - 1));
            if (
                indentLength >= Math.min(indentLength, textLength)
                    && !text.substring(0, endIndex).equals(myIndentString.substring(0, endIndex))
            ) {
                error("Tab/space mixing");
                return false;

            } else {
                String indent = determineCurrentIndent(text);
                if (indent.length() > 0) {
                    myIndentStrings.add(indent);
                }
                myIndentString = text;
            }

        } catch (StringIndexOutOfBoundsException e) {
            /*Notification notification = new Notification(
                    NOTIFICATION_GROUP,
                    "X",
                    "=" + textLength + "-" + indentLength + "=",
                    NotificationType.INFORMATION
            );
            Application app = ApplicationManager.getApplication();
            if (!app.isDisposed()) {
                app.getMessageBus().syncPublisher(Notifications.TOPIC).notify(notification);
            } else {
                throw e;
            }*/
        }
        return myIndentStrings.size() <= 0 || myIndentString.length() >= repeatString(myIndentStrings.get(0), depth).length();
    }

    private static String repeatString(String s, int max) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < max; i++) {
            out.append(s);
        }
        return out.toString();
    }

    private static String determineCurrentIndent(String text) {
        for (String indent : myIndentStrings.toArray(new String[0])) {
            if (text.length() >= indent.length()) {
                text = text.substring(indent.length());
            }
        }
        return text;
    }

    private static boolean isIndentsValid() {
        int length = 0;
        for (String indent : myIndentStrings.toArray(new String[0])) {
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
