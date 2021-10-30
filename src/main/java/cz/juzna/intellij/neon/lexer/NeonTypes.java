package cz.juzna.intellij.neon.lexer;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class NeonTypes implements _NeonTypes {

    public static TokenSet WHITESPACES = TokenSet.create(T_WHITESPACE, TokenType.WHITE_SPACE);
    public static TokenSet COMMENTS = TokenSet.create(T_COMMENT);
    public static TokenSet STRING_LITERALS = TokenSet.create(T_LITERAL, T_STRING);
    public static TokenSet KEY_ELEMENTS = TokenSet.create(T_STRING, T_LITERAL, T_NUMBER, T_ARRAY_BULLET);

    public static @NotNull PsiElement createElement(ASTNode node) {
        return _NeonTypes.Factory.createElement(node);
    }

}
