package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static cz.juzna.intellij.neon.lexer.NeonTokenTypes.*;

/**
 * @author Jan Dolecek
 * @author Jan Tvrdík
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

STRING = \'[^\'\n]*\'|\"(\\.|[^\"\\\n])*\"
COMMENT = \#.*
INDENT = \n[\t ]*
LITERAL_START = [^#\"\',=\[\]{}()\x00-\x20!`]
WHITESPACE = [\t ]+

%state IN_LITERAL

%%

<YYINITIAL> {

    {STRING} {
        return NEON_STRING;
    }

    "-" / [ \n] { return NEON_ARRAY_BULLET; }
    "-" $ { return NEON_ARRAY_BULLET; }
    ":" / [ \n,\]})] { return NEON_COLON; }
    ":" $ { return NEON_COLON; }
    "," { return NEON_ITEM_DELIMITER; }
    "=" { return NEON_ASSIGNMENT; }

    "(" { return NEON_LPAREN; }
    ")" { return NEON_RPAREN; }
    "{" { return NEON_LBRACE_CURLY; }
    "}" { return NEON_RBRACE_CURLY
    "[" { return NEON_LBRACE_SQUARE; }
    "]" { return NEON_RBRACE_SQUARE; }

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

    . {
        return NEON_UNKNOWN;
    }
}

<IN_LITERAL> {
    [^,:=\]})(\x00-\x20]+ {}
    [ \t]+[^#,:=\]})(\x00-\x20] {}
    ":"[ \t\n,\]})] { retryInState(YYINITIAL); }
    ":"$ { retryInState(YYINITIAL); }
    ":" {}
    .|\n { retryInState(YYINITIAL); }
}
