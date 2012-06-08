package cz.juzna.intellij.neon.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.NeonLanguage;

/**
 * @author Jan Dolecek - juzna.cz@gmail.com
 */
public interface NeonTokenTypes {
    IElementType NEON_KEYWORD = new NeonElementType("keyword");
    IElementType NEON_EOL = new NeonElementType("eol");
    IElementType NEON_INTERPUNCTION = new NeonElementType("interpunction");
    IElementType NEON_BLOCK = new NeonElementType("block");
    IElementType NEON_VALUED_BLOCK = new NeonElementType("valuedblock");
    IElementType NEON_STRING = new NeonElementType("string");
    IElementType NEON_COMMENT = new NeonElementType("comment");
    IElementType NEON_UNKNOWN = TokenType.BAD_CHARACTER; //new NeonElementType("error");
    IElementType NEON_LITERAL = new NeonElementType("literal");
    IElementType NEON_VARIABLE = new NeonElementType("variable");
    IElementType NEON_NUMBER = new NeonElementType("number");
    IElementType NEON_REFERENCE = new NeonElementType("reference");
    IElementType NEON_WHITESPACE = TokenType.WHITE_SPACE; //new NeonElementType("whitespace");
    


    IFileElementType FILE = new IFileElementType("FILE", NeonLanguage.LANGUAGE);

    TokenSet WHITESPACES = TokenSet.create(NEON_WHITESPACE);
    TokenSet COMMENTS = TokenSet.create(NEON_COMMENT);
    TokenSet STRING_LITERALS = TokenSet.create(NEON_LITERAL);
}
