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

{STRING} {
    return NEON_STRING;
}

"-" / \s { return NEON_SYMBOL; }
"-" $  { return NEON_SYMBOL; }
":" / [\s,\]\}\)] { return NEON_SYMBOL; }
":" $ { return NEON_SYMBOL; }


{COMMENT} {
    return NEON_COMMENT;
}

{INDENT} {
    return NEON_INDENT;
}

{WHITESPACE} {
    return NEON_WHITESPACE;
}

[^#\"\',=\[\]{}()\x00-\x20!`] {
    yybegin(LITERAL_INIT);
}




// LITERAL = [^#\"\',=\[\]{}()\x00-\x20!`]([^,:=\]})(\x00-\x20]+|:(?![\s,\]})]|$)|[\ \t]+[^#,:=\]})(\x00-\x20])*
<LITERAL_INIT> {
    [^,:=\]})(\x00-\x20]+ {

    }

    :[\s,\]})] {
        yypushback(2);
        yybegin(YYINITIAL);
        return NEON_LITERAL;
    }

    ":" {

    }

    [\ \t]+[^#,:=\]})(\x00-\x20] {

    }

    . {
        yypushback(2);
        yybegin(YYINITIAL);
        return NEON_LITERAL;
    }

}

// default
{WHITESPACE} { return NEON_WHITESPACE; }
.            { return NEON_UNKNOWN; }