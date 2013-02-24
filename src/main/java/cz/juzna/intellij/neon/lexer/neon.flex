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
COMMENT = \#.*
INDENT = \n[\t\ ]*
LITERAL_START = [^#\"\',=\[\]{}()\x00-\x20!`]
WHITESPACE = [\t\ ]+

%state IN_LITERAL

%%

<YYINITIAL> {

    {STRING} {
        return NEON_STRING;
    }

    "-" / [ \n] { return NEON_SYMBOL; }
    "-" $  { return NEON_SYMBOL; }
    ":" / [ \n,\]})] { return NEON_SYMBOL; }
    ":" $ { return NEON_SYMBOL; }
    [,=\[\]{}()] { return NEON_SYMBOL; }

    {COMMENT} {
        return NEON_COMMENT;
    }

    {INDENT} {
        return NEON_INDENT;
    }

    {LITERAL_START} {
        yybegin(IN_LITERAL);
        return NEON_LITERAL;
    }

    {WHITESPACE} {
        return NEON_WHITESPACE;
    }
}

<IN_LITERAL> {
    [^,:=\]})(\x00-\x20]+ {}
    [ \t]+[^#,:=\]})(\x00-\x20] {}
    ":"[ \n,\]\}\)] { retryInState(YYINITIAL); }
    ":" {}
    .|\n  { retryInState(YYINITIAL); }
}
