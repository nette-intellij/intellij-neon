package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static cz.juzna.intellij.neon.lexer.NeonTokenTypes.*;

/**
 * @author Jan Dolecek
 */
%%

%class _NeonLexer
%implements FlexLexer
%public
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%{
	private void retryInState(int newState) {
        yybegin(newState);
        yypushback(yylength());
	}
%}


STRING = \'[^\'\n]*\'|\"(\\\\.|[^\"\\\\\n])*\"
//SYMBOL = :(?=[\s,\]})]|$)| [,=[\]{}()]
COMMENT = \#.*
INDENT = \n[\t\ ]*
// LITERAL = [^#\"\',=\[\]{}()\x00-\x20!`]([^,:=\]})(\x00-\x20]+|:(?![\s,\]})]|$)|[\ \t]+[^#,:=\]})(\x00-\x20])*
WHITESPACE = [\t\ ]+

%state LITERAL_INIT

%%

<YYINITIAL> {

    {STRING} {
        return NEON_STRING;
    }

    "-" / [ \n] { return NEON_SYMBOL; }
    "-" $  { return NEON_SYMBOL; }
    ":" / [ \n,\]\}\)] { return NEON_SYMBOL; }
    ":" $ { return NEON_SYMBOL; }
    [,=\[\]{}()] { return NEON_SYMBOL; }


    {COMMENT} {
        return NEON_COMMENT;
    }

    {INDENT} {
        return NEON_INDENT;
    }

    [^#\"\',=\[\]{}()\x00-\x20!`] {
        yybegin(LITERAL_INIT);
        return NEON_LITERAL;
    }

    {WHITESPACE} {
        return NEON_WHITESPACE;
    }
}

<LITERAL_INIT> {
    [^,:=\]})(\x00-\x20]+ {}
    ":"[ \n,\]\}\)] {
        yypushback(2);
        yybegin(YYINITIAL);
    }
    ":" {}
    [ \t]+[^#,:=\]})(\x00-\x20] {}
    .|\n {
        yypushback(1);
        yybegin(YYINITIAL);
    }
}
