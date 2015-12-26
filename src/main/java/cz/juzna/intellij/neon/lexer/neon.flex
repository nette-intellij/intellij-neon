package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static cz.juzna.intellij.neon.lexer.NeonTokenTypes.*;

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
LITERAL_START = [^:#\"\',=\[\]{}()\x00-\x20!`]|:[\x21-x28*\x2D-\x5C\x5E-\x7C~\xA0-\uFFFF]
WHITESPACE = [\t ]+

%state IN_LITERAL

%%

<YYINITIAL> {

    {STRING} {
        return NEON_STRING;
    }

    "-" / [ \t\n] { return NEON_ARRAY_BULLET; }
    "-" $ { return NEON_ARRAY_BULLET; }
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
    ":" { retryInState(YYINITIAL); }
    .|\n { retryInState(YYINITIAL); }
}
