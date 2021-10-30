package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static cz.juzna.intellij.neon.lexer.NeonTypes.*;

/**
 * @author Jan Dolecek
 * @author Jan Tvrdík
 * @author Michael Moravec
 */
%%

%class _NeonLexer2
%implements FlexLexer
%public
%unicode
%function advance
%type IElementType

%{
    private void retryInState(int newState) {
        yybegin(newState);
        yypushback(yylength());
    }
%}

STRING = \'[^\'\n]*\'|\"(\\.|[^\"\\\n])*\"
DATE = (\d{4})-(\d{2})-(\d{2})
DATE_TIME = {DATE}\s(\d{2}):(\d{2}):(\d{2})
BIN_NUMBER = [+-]?0[bB][01]+*
OCT_NUMBER = [+-]?0[oO][1-7][0-7]*
HEX_NUMBER = [+-]?0[xX][0-9a-fA-F]+
NUMBER = [+-]?[0-9]+(\.[0-9]+)?([Ee][+-]?[0-9]+)?
COMMENT = \#.*
INDENT = \n[\t ]*
LITERAL_START = [^-:#\"\',=\[\]{}()\x00-\x20!`]|[:-][!#$%&*\x2D-\x5C\x5E-\x7C~\xA0-\uFFFF]
WHITESPACE = [\t ]+

%states DEFAULT, IN_LITERAL, VYINITIAL, IN_MULTILINE_DQ, IN_MULTILINE_SQ

%%

<YYINITIAL> {

    {WHITESPACE} {
        return T_INDENT;
    }
    .|\n {
        retryInState(DEFAULT);
    }
}

<DEFAULT> {
    "\"\"\"" / \n((\n|.)*\n)?[ \t]*"\"\"\"" {
        yybegin(IN_MULTILINE_DQ);
        return T_STRING;
    }
    "'''" / \n((\n|.)*\n)?[ \t]*"'''" {
        yybegin(IN_MULTILINE_SQ);
        return T_STRING;
    }
    {STRING} {
        return T_STRING;
    }

    "," { return T_ITEM_DELIMITER; }
    "=" { return T_ASSIGNMENT; }
    "=>" { return T_DOUBLE_ARROW; }

    "(" { return T_LPAREN; }
    ")" { return T_RPAREN; }
    "{" { return T_LBRACE_CURLY; }
    "}" { return T_RBRACE_CURLY; }
    "[" { return T_LBRACE_SQUARE; }
    "]" { return T_RBRACE_SQUARE; }

    {COMMENT} {
        return T_COMMENT;
    }

    {DATE_TIME} | {DATE} {
        return T_DATE_TIME;
    }

    {NUMBER} | {BIN_NUMBER} | {HEX_NUMBER} | {OCT_NUMBER} {
        return T_NUMBER;
    }

    [+-]?[0-9]+({NUMBER})* {
        return T_STRING;
    }

    {INDENT} {
        return T_INDENT;
    }

    {LITERAL_START} {
        yybegin(IN_LITERAL);
        return T_LITERAL;
    }

    ":" { return T_COLON; }
    "::" { return T_DOUBLE_COLON; }
    "." { return T_CONCATENATION; }
    "-" { return T_ARRAY_BULLET; }

    {WHITESPACE} {
        return T_WHITESPACE;
    }

    . {
        return T_UNKNOWN;
    }
}

<IN_LITERAL> {
    [^,:=\]})(\x00-\x20]+ {}
    [ \t]+[^#,:=\]})(\x00-\x20] {}
    ":" / [\x21-x28*\x2D-\x5C\x5E-\x7C~\xA0-\uFFFF] { }
    ":" { retryInState(DEFAULT); }
    .|\n { retryInState(DEFAULT); }
}

<IN_MULTILINE_DQ> {
    \n[ \t]*"\"\"\"" {
        yybegin(DEFAULT);
    }
    .|\n {}
}
<IN_MULTILINE_SQ> {
    \n[ \t]*"'''" {
        yybegin(DEFAULT);
    }
    .|\n {}
}
