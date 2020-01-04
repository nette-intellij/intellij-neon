package cz.juzna.intellij.neon.lexer;

import com.intellij.psi.tree.IElementType;
import static NeonTokenTypes.*;

/**
 * @author Jan Dolecek
 * @author Jan Tvrdík
 * @author Michael Moravec
 */
%%

%class _NeonLexer
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
COMMENT = \#.*
INDENT = \n[\t ]*
LITERAL_START = [^-:#\"\',=\[\]{}()\x00-\x20!`]|[:-][!#$%&*\x2D-\x5C\x5E-\x7C~\xA0-\uFFFF]
WHITESPACE = [\t ]+

%states DEFAULT, IN_LITERAL, VYINITIAL, IN_MULTILINE_DQ, IN_MULTILINE_SQ

%%

<YYINITIAL> {

    {WHITESPACE} {
        return NEON_INDENT;
    }
    .|\n {
        retryInState(DEFAULT);
    }
}

<DEFAULT> {
    "\"\"\"" / \n((\n|.)*\n)?[ \t]*"\"\"\"" {
        yybegin(IN_MULTILINE_DQ);
        return NEON_STRING;
    }
    "'''" / \n((\n|.)*\n)?[ \t]*"'''" {
        yybegin(IN_MULTILINE_SQ);
        return NEON_STRING;
    }
    {STRING} {
        return NEON_STRING;
    }

    "," { return NEON_ITEM_DELIMITER; }
    "=" { return NEON_ASSIGNMENT; }

    "(" { return NEON_LPAREN; }
    ")" { return NEON_RPAREN; }
    "{" { return NEON_LBRACE_CURLY; }
    "}" { return NEON_RBRACE_CURLY; }
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

    ":" { return NEON_COLON; }
    "-" { return NEON_ARRAY_BULLET; }

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
