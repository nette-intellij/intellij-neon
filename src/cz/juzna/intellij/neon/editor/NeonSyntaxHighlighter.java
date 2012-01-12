package cz.juzna.intellij.neon.editor;


import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.lexer.NeonLexer;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class NeonSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final String            UNKNOWN_ID       = "Bad character";
    public static final TextAttributesKey UNKNOWN          = TextAttributesKey.createTextAttributesKey(UNKNOWN_ID, HighlighterColors.BAD_CHARACTER.getDefaultAttributes().clone());

    public static final String            COMMENT_ID       = "Latte comment";
    public static final TextAttributesKey COMMENT          = TextAttributesKey.createTextAttributesKey(COMMENT_ID, SyntaxHighlighterColors.LINE_COMMENT.getDefaultAttributes().clone());

    public static final String            BLOCK_ID         = "Block";
    public static final TextAttributesKey BLOCK            = TextAttributesKey.createTextAttributesKey(BLOCK_ID, SyntaxHighlighterColors.DOC_COMMENT.getDefaultAttributes().clone());

    public static final String            INTERPUNCTION_ID = "Interpunction";
    public static final TextAttributesKey INTERPUNCTION    = TextAttributesKey.createTextAttributesKey(BLOCK_ID, SyntaxHighlighterColors.DOT.getDefaultAttributes().clone());
    
    public static final String            NUMBER_ID        = "Number";
    public static final TextAttributesKey NUMBER           = TextAttributesKey.createTextAttributesKey(NUMBER_ID, SyntaxHighlighterColors.NUMBER.getDefaultAttributes().clone());

    public static final String            KEYWORD_ID       = "Keyword";
    public static final TextAttributesKey KEYWORD          = TextAttributesKey.createTextAttributesKey(KEYWORD_ID, SyntaxHighlighterColors.KEYWORD.getDefaultAttributes().clone());


    // Groups of IElementType's
    public static final TokenSet sBAD           = TokenSet.create(NeonTokenTypes.NEON_UNKNOWN);
    public static final TokenSet sCOMMENTS      = TokenSet.create(NeonTokenTypes.NEON_COMMENT);
    public static final TokenSet sBLOCKS        = TokenSet.create(NeonTokenTypes.NEON_BLOCK, NeonTokenTypes.NEON_VALUED_BLOCK);
    public static final TokenSet sINTERPUNCTION = TokenSet.create(NeonTokenTypes.NEON_INTERPUNCTION);
    public static final TokenSet sNUMBERS       = TokenSet.create(NeonTokenTypes.NEON_NUMBER);


    // Static container
    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();


    // Fill in the map
    static {
        fillMap(ATTRIBUTES, sBAD,           UNKNOWN);
        fillMap(ATTRIBUTES, sCOMMENTS,      COMMENT);
        fillMap(ATTRIBUTES, sBLOCKS,        BLOCK);
        fillMap(ATTRIBUTES, sINTERPUNCTION, INTERPUNCTION);
        fillMap(ATTRIBUTES, sNUMBERS,       NUMBER);
    }


    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new NeonLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType type) {
        return pack(ATTRIBUTES.get(type));
    }
}
