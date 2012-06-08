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
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}



KEYWORD=("true" | "TRUE" | "false" | "FALSE" | "yes" | "YES" | "no" | "NO" | "null" | "NULL")
WHITESPACE=[ \t]
NEWLINE=("\r"|"\n"|"\r\n")

// numbers
ZERO=0
DECIMAL=[\+\-]?[1-9][0-9]*
OCTAL=0[0-7]+
HEXADECIMAL=0[xX][0-9A-Fa-f]+
EXPONENT=[eE][\+\-]?[0-9]+
FLOAT_1=[0-9]+\.[0-9]+{EXPONENT}?
FLOAT_2=\.[0-9]+{EXPONENT}?
FLOAT_3=[0-9]+\.{EXPONENT}?
FLOAT_4=[0-9]+{EXPONENT}
FLOAT={FLOAT_1} | {FLOAT_2} | {FLOAT_3} | {FLOAT_4}
NUMBER={ZERO} | {DECIMAL} | {OCTAL} | {HEXADECIMAL} | {FLOAT}

S_STRING = "'"([^'\r\n])*"'"
D_STRING = "\""(\\.|[^\"\\\n]*)"\""
STRING = {D_STRING} | {S_STRING}


IDENTIFIER=[[:letter:]_\x7f-\xff][[:letter:][:digit:]_\x7f-\xff\.\\]*"!"?
LITERAL=([^#%\"',=\[\]\{\}\(\)\<\>\t\n\r@ ])+

VARIABLE="%"{LITERAL}"%"?
REFERENCE="@"({IDENTIFIER} | "\\")+
COMMENT="#"[^"\r""\n""\r\n")]*



%%
<YYINITIAL> {
    // 1: strings
    {STRING} {
        return NEON_STRING;
    }

    // 2a: symbols
    ":" / ( {WHITESPACE}+ | {NEWLINE} ) {
        return NEON_ASSIGNMENT;
    }
    "-" / ( {WHITESPACE}+ | {NEWLINE} ) {
        return NEON_ARRAY_BULLET;
    }
    "," {
        return NEON_ITEM_DELIMITER;
    }
    "=" {
        return NEON_ASSIGNMENT;
    }

    // 2b: braces
    "[" {
        return NEON_LBRACE_SQUARE;
    }
    "{" {
        return NEON_LBRACE_CURLY;
    }
    "(" {
        return NEON_LPAREN;
    }
    "]" {
        return NEON_RBRACE_SQUARE;
    }
    "}" {
        return NEON_RBRACE_CURLY;
    }
    ")" {
        return NEON_RPAREN;
    }


    // 3: comment
    {COMMENT} {
        return NEON_COMMENT;
    }


    // 4: new line + indent
    {NEWLINE}{WHITESPACE}* {
        return NEON_INDENT;
    }



    // 5: literal / boolean / ...
    {IDENTIFIER} {
        return NEON_IDENTIFIER;
    }
    "<" {
        return NEON_BLOCK_INHERITENCE;
    }
    {REFERENCE} {
        return NEON_REFERENCE;
    }
    {KEYWORD} {
        return NEON_KEYWORD;
    }
    {NUMBER} {
        return NEON_NUMBER;
    }
//    {LITERAL} {
//        return NEON_LITERAL;
//    }
    {VARIABLE} {
        return NEON_VARIABLE;
    }
    {STRING} {
        return NEON_STRING;
    }
}

// default
{WHITESPACE} { return NEON_WHITESPACE; }
.            { return NEON_UNKNOWN; }
