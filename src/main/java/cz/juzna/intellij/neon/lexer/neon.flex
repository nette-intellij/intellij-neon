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
LITERAL = [^#\"\',=\[\]{}()\x00-\x20!`]([^,:=\]})(\x00-\x20]+|:(?![\s,\]})]|$)|[\ \t]+[^#,:=\]})(\x00-\x20])*
WHITESPACE = [\t\ ]+


%%

{STRING} {
    return NEON_STRING;
}

"-" / \s |
"-" / $  |
":" / [\s,\]\}\)] |
":" / $ {
	return NEON_SYMBOL;
}

{COMMENT} {
    return NEON_COMMENT;
}

{INDENT} {
    return NEON_INDENT;
}

{LITERAL} {
    return NEON_LITERAL;
}
{WHITESPACE} {
    return NEON_WHITESPACE;
}


// default
//{WHITESPACE} { return NEON_WHITESPACE; }
.            { return NEON_UNKNOWN; }
