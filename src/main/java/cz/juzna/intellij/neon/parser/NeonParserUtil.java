package cz.juzna.intellij.neon.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static cz.juzna.intellij.neon.lexer.NeonTypes.*;

public class NeonParserUtil extends GeneratedParserUtilBase {

    private static @Nullable IndentMatcher indentMatcher;

    public static boolean initIndentMatcher(PsiBuilder builder, int level) {
        indentMatcher = new IndentMatcher();
        return true;
    }

    private static @NotNull IndentMatcher getIndentMatcher() {
        assert indentMatcher != null;
        return indentMatcher;
    }

    public static boolean isComment(PsiBuilder builder, int level) {
        if (builder.getTokenType() != T_INDENT) {
            return builder.getTokenType() == T_COMMENT;
        }
        return getNextTokenAfterIndent(builder) == T_COMMENT;
    }

    private enum Check {
        SAME_LEVEL,
        INNER,
    }

    public static boolean isInnerKeyValPair(PsiBuilder builder, int level) {
        return isKeyValPair(builder, level, Check.INNER);
    }

    public static boolean isSameKeyValPair(PsiBuilder builder, int level) {
        return isKeyValPair(builder, level, Check.SAME_LEVEL);
    }

    private static boolean isKeyValPair(PsiBuilder builder, int level, Check check) {
        IElementType token = builder.getTokenType();
        if (token != T_INDENT) {
            return KEY_ELEMENTS.contains(token);
        }

        String indent = getLastTokenWithType(builder, T_INDENT);
        if (indent == null) {
            return true;
        }

        String current = normalizeIndent(indent);
        boolean success = getIndentMatcher().addIfAbsent(current);
        if (!success) {
            builder.error("Bad indent");
            return false;
        }

        int indentLevel = getIndentMatcher().getLevel(current);
        if (indentLevel < 0) {
            builder.error("Bad indent");
            return false;
        }

        indentLevel = (indentLevel + 1) * 4;
        if (check == Check.INNER) {
            return indentLevel > (level - 4);
        } else if (check == Check.SAME_LEVEL) {
            return indentLevel == ((level % 4 == 0) ? level : (level - 2));
        }
        return indentLevel < level;
    }

    private static @NotNull String normalizeIndent(@NotNull String indent) {
        return indent.replace("\n", "");
    }

    private static @Nullable String getLastTokenWithType(PsiBuilder builder, IElementType type) {
        PsiBuilder.Marker marker = builder.mark();
        String indent = getLastTokenWithType(builder, type, builder.getTokenText());
        marker.rollbackTo();
        return indent;
    }

    private static @Nullable IElementType getNextTokenAfterIndent(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        IElementType nextToken = getNextTokenAfterIndent(builder, T_INDENT);
        marker.rollbackTo();
        return nextToken;
    }

    private static @Nullable IElementType getNextTokenAfterIndent(PsiBuilder builder, IElementType type) {
        if (builder.getTokenType() == type) {
            builder.advanceLexer();
            return getNextTokenAfterIndent(builder, type);
        }
        return builder.getTokenType();
    }

    private static @Nullable String getLastTokenWithType(PsiBuilder builder, IElementType type, @Nullable String prevIndent) {
        if (builder.getTokenType() == type) {
            String current = builder.getTokenText();
            builder.advanceLexer();
            return getLastTokenWithType(builder, type, current);
        }
        return prevIndent;
    }

    public static class IndentMatcher {
        private static final int ERROR_INVALID = -1;
        private static final int ERROR_ADDITIONS = -2;

        private final List<String> indents = new ArrayList<>();
        private int length = 0;

        public IndentMatcher() {}

        @Override
        public String toString() {
            return String.join("", indents);
        }

        List<String> getIndents() {
            return indents;
        }

        public boolean addIfAbsent(String indent) {
            int level = getLevel(indent);
            if (level != ERROR_ADDITIONS) {
                return level != ERROR_INVALID;
            }

            String addition = indent.substring(length);
            if (addition.length() > 0) {
                indents.add(addition);
                length += addition.length();
            }
            return true;
        }

        public boolean match(String indent) {
            return getLevel(indent) < 0;
        }

        public int getLevel(String indent) {
            int level = 0;
            int offset = 0;
            for (String current : indents) {
                int currentLength = current.length();
                if (offset + currentLength > indent.length()) {
                    if (indent.substring(offset).length() > 0) {
                        return ERROR_INVALID;
                    }
                    break;
                }

                if (indent.substring(offset, offset + currentLength).equals(current)) {
                    level++;
                    offset += currentLength;
                } else {
                    return ERROR_INVALID;
                }
            }

            if (indent.substring(offset).length() > 0) {
                return ERROR_ADDITIONS;
            }
            return level;
        }
    }

}
