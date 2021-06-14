package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static cz.juzna.intellij.neon.lexer.NeonTokenTypes.*;

/**
 * @author Jan Dolecek
 * @author Jan Tvrdík
 * @author Michael Moravec
 * @author Matouš Němec
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

DATE = (\d{4})-(\d{2})-(\d{2})
DATE_TIME = {DATE}\s(\d{2}):(\d{2}):(\d{2})
BIN_NUMBER = [+-]?0[bB][01]+*
OCT_NUMBER = [+-]?0[oO][1-7][0-7]*
HEX_NUMBER = [+-]?0[xX][0-9a-fA-F]+
NUMBER = [+-]?[0-9]+(\.[0-9]+)?([Ee][+-]?[0-9]+)?
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*
KEY_IDENTIFIER=[a-zA-Z0-9_!$&*><][a-zA-Z0-9_!$&*><]*
SHORTCUT_CLASS_NAME=\\?[a-zA-Z_][a-zA-Z0-9_\\]*
COMMENT = \#.*
INDENT = \n+[\t ]*
LITERAL_START = [^-:#\"\',=\[\]{}()\x00-\x20!`]|[:-][!#$&*><\x2D-\x5C\x5E-\x7C~\xA0-\uFFFF]
WHITESPACE = [\t ]+

%states DEFAULT, IN_LITERAL, VYINITIAL

%state IN_PARAMETER
%state IN_PARAMETER_DOUBLE
%state IN_PARAMETER_SINGLE
%state SINGLE_QUOTED
%state DOUBLE_QUOTED
%state CLASS_REFERENCE
%state STATIC_FIELD
%state STATIC_METHOD

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

    "\\" / {IDENTIFIER} {
        yybegin(CLASS_REFERENCE);
        return NEON_NAMESPACE_RESOLUTION;
    }

    {IDENTIFIER} / ("\\") {
        yybegin(CLASS_REFERENCE);
        return NEON_NAMESPACE_REFERENCE;
    }

    {IDENTIFIER} {
        return NEON_IDENTIFIER;
    }

    {IDENTIFIER} / {LITERAL_START} {
        yybegin(IN_LITERAL);
        return NEON_LITERAL;
    }

    "'"  {
    	yybegin(SINGLE_QUOTED);
    	return NEON_SINGLE_QUOTE_LEFT;
    }

    "%" / ({KEY_IDENTIFIER} "."?)+ "%" {
    	yybegin(IN_PARAMETER);
    	return NEON_PARAMETER_LEFT;
    }

    "\"" {
        yybegin(DOUBLE_QUOTED);
        return NEON_DOUBLE_QUOTE_LEFT;
    }

    {NUMBER} | {BIN_NUMBER} | {HEX_NUMBER} | {OCT_NUMBER} {
        return NEON_NUMBER;
    }

    {DATE_TIME} | {DATE} {
        return NEON_DATE_TIME;
    }

    [+-]?[0-9]+({NUMBER})* {
        return NEON_STRING;
    }

    {IDENTIFIER} / ("(") {
        return NEON_METHOD;
    }

    {IDENTIFIER} / ("::" {IDENTIFIER}) {
        return NEON_IDENTIFIER;
    }

    {SHORTCUT_CLASS_NAME} "*" {
        yybegin(IN_LITERAL);
        return NEON_LITERAL;
    }

    [a-zA-Z0-9_!$&*><][a-zA-Z0-9_!$%&*><]+ | {IDENTIFIER} {WHITESPACE} [!$&*><]+ {WHITESPACE} {IDENTIFIER} {
        return NEON_LITERAL;
    }

    "::" / {IDENTIFIER} {
        yybegin(STATIC_FIELD);
        return NEON_DOUBLE_COLON;
    }

    "::" / {IDENTIFIER} "(" {
        yybegin(STATIC_METHOD);
        return NEON_DOUBLE_COLON;
    }

    "," { return NEON_ITEM_DELIMITER; }
    "." { return NEON_CONCATENATION; }
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

    {LITERAL_START} {
        yybegin(IN_LITERAL);
        return NEON_LITERAL;
    }

    "@" / {IDENTIFIER} "\\" {
        return NEON_KEY_USAGE;
    }

    "@" {KEY_IDENTIFIER} / "::" {IDENTIFIER} {
        return NEON_KEY_USAGE;
    }

    "@" {KEY_IDENTIFIER} {
        return NEON_KEY_USAGE;
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

<CLASS_REFERENCE> {
    {IDENTIFIER} {
        yybegin(DEFAULT);
        return NEON_NAMESPACE_REFERENCE;
    }

    "\\" {
        yybegin(DEFAULT);
        return NEON_NAMESPACE_RESOLUTION;
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

<STATIC_METHOD> {
    {IDENTIFIER} {
        return NEON_PHP_STATIC_METHOD;
    }

    "(" {
        yybegin(DEFAULT);
        return NEON_LPAREN;
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

<IN_PARAMETER> {
	"%" {
		yybegin(DEFAULT);
		return NEON_PARAMETER_RIGHT;
	}
	"." {
		return NEON_PARAMETER_DELIMITER;
	}
	("\\" [^] | [^'\\%\.])+ {
		return NEON_PARAMETER_USAGE;
	}
	.|\n {}
}

<IN_PARAMETER_DOUBLE> {
	"%" {
		yybegin(DOUBLE_QUOTED);
		return NEON_PARAMETER_RIGHT;
	}
	"." {
		return NEON_PARAMETER_DELIMITER;
	}
	("\\" [^] | [^'\\%\.])+ {
		return NEON_PARAMETER_USAGE;
	}
	.|\n {}
}

<IN_PARAMETER_SINGLE> {
	"%" {
		yybegin(SINGLE_QUOTED);
		return NEON_PARAMETER_RIGHT;
	}
	"." {
		return NEON_PARAMETER_DELIMITER;
	}
	("\\" [^] | [^'\\%\.])+ {
		return NEON_PARAMETER_USAGE;
	}
	.|\n {}
}

<SINGLE_QUOTED> {
	"'" {
		yybegin(DEFAULT);
		return NEON_SINGLE_QUOTE_RIGHT;
	}
	("\\" [^] | [^'\\%])+ {
		return NEON_STRING;
	}
    "%" / ({KEY_IDENTIFIER} "."?)+ "%" {
        yybegin(IN_PARAMETER_SINGLE);
        return NEON_PARAMETER_LEFT;
    }
	"%" {}
	.|\n {}
}

<DOUBLE_QUOTED> {
	"\"" {
		yybegin(DEFAULT);
		return NEON_DOUBLE_QUOTE_RIGHT;
	}
	("\\" [^] | [^\"\\%])+ {
		return NEON_STRING;
	}
	"%" / ({KEY_IDENTIFIER} "."?)+ "%" {
        yybegin(IN_PARAMETER_DOUBLE);
        return NEON_PARAMETER_LEFT;
    }
	"%" {}
	.|\n {}
}
