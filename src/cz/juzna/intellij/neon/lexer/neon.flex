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

NUMBER = [+-]?[0-9]+(\.[0-9]+)?([Ee][+-]?[0-9]+)?
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*
CLASS_NAME=@?(\\?[a-zA-Z_][a-zA-Z0-9_\*]*\\[a-zA-Z_\*][a-zA-Z0-9_\*\\]* | \\[a-zA-Z_\*][a-zA-Z0-9_\*]*)
COMMENT = \#.*
INDENT = \n[\t ]*
LITERAL_START = [^-:#\"\',=\[\]{}()\x00-\x20!`]|[:-][!#$%&*\x2D-\x5C\x5E-\x7C~\xA0-\uFFFF]
WHITESPACE = [\t ]+

%states DEFAULT, IN_LITERAL, VYINITIAL

%state SINGLE_QUOTED
%state DOUBLE_QUOTED
%state STATIC_FIELD

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

    {CLASS_NAME} {
        return NEON_CLASS_NAME;
    }

    "'"  {
    	yybegin(SINGLE_QUOTED);
    	return NEON_SINGLE_QUOTE_LEFT;
    }

    "\"" {
        yybegin(DOUBLE_QUOTED);
        return NEON_DOUBLE_QUOTE_LEFT;
    }

    {NUMBER} {
        return NEON_NUMBER;
    }

    [+-]?[0-9]+({NUMBER})* {
        return NEON_STRING;
    }

    {IDENTIFIER} / ("(") {
        return NEON_METHOD;
    }

    "::" / ({IDENTIFIER}) {
        yybegin(STATIC_FIELD);
        return NEON_DOUBLE_COLON;
    }

    "," { return NEON_ITEM_DELIMITER; }
    "=" { return NEON_ASSIGNMENT; }
    "::" { return NEON_DOUBLE_COLON; }

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

    ({LITERAL_START} | {NUMBER} {LITERAL_START}) {
        yybegin(IN_LITERAL);
        return NEON_LITERAL;
    }

    "%" {LITERAL_START} "%" {
        yybegin(IN_LITERAL);
        return NEON_PARAMETER_USAGE;
    }

    "@" {LITERAL_START} {
        yybegin(IN_LITERAL);
        return NEON_KEY_USAGE;
    }

    {NUMBER} [\s\t] {LITERAL_START} {
        yybegin(IN_LITERAL);
        return NEON_STRING;
    }

    ":" { return NEON_COLON; }
    "-" { return NEON_ARRAY_BULLET; }

    {WHITESPACE} {
        return NEON_WHITESPACE;
    }

    [^] {
        return NEON_UNKNOWN;
    }
}

<STATIC_FIELD> {
    {IDENTIFIER} {
        yybegin(DEFAULT);
        return NEON_PHP_STATIC_IDENTIFIER;
    }

    {LITERAL_START} {
        yybegin(DEFAULT);
        return NEON_LITERAL;
    }
}

<IN_LITERAL> {
    [^,:=\]})(\x00-\x20]+ {}
    [ \t]+[^#,:=\]})(\x00-\x20] {}
    ":" / [\x21-x28*\x2D-\x5C\x5E-\x7C~\xA0-\uFFFF] { }
    ":" { retryInState(DEFAULT); }
    .|\n { retryInState(DEFAULT); }
}

<SINGLE_QUOTED> {
	"'" {
		yybegin(DEFAULT);
		return NEON_SINGLE_QUOTE_RIGHT;
	}
	("\\" [^] | [^'\\])+ {
		return NEON_STRING;
	}
	.|\n {}
}

<DOUBLE_QUOTED> {
	"\"" {
		yybegin(DEFAULT);
		return NEON_DOUBLE_QUOTE_RIGHT;
	}
	("\\" [^] | [^\"\\])+ {
		return NEON_STRING;
	}
	.|\n {}
}
