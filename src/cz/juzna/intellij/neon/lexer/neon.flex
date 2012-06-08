package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;

/**
 * @author Ondrej Brejla
 */
%%

%class _NeonLexer
%implements FlexLexer, NeonTokenTypes
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%{
    private StateStack stack = new StateStack();

    public _NeonLexer() {
        zzState = zzLexicalState = YYINITIAL;
        stack.clear();
    }

    public static final class LexerState  {
        final StateStack stack;
        /** the current state of the DFA */
        final int zzState;
        /** the current lexical state */
        final int zzLexicalState;

        LexerState(StateStack stack, int zzState, int zzLexicalState) {
            this.stack = stack;
            this.zzState = zzState;
            this.zzLexicalState = zzLexicalState;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            LexerState state = (LexerState) obj;
            return (this.stack.equals(state.stack)
                && (this.zzState == state.zzState)
                && (this.zzLexicalState == state.zzLexicalState));
        }

        @Override
        public int hashCode() {
            int hash = 11;
            hash = 31 * hash + this.zzState;
            hash = 31 * hash + this.zzLexicalState;
            if (stack != null) {
                hash = 31 * hash + this.stack.hashCode();
            }
            return hash;
        }
    }

    public LexerState getState() {
        return new LexerState(stack.createClone(), zzState, zzLexicalState);
    }

    public void setState(LexerState state) {
        this.stack.copyFrom(state.stack);
        this.zzState = state.zzState;
        this.zzLexicalState = state.zzLexicalState;
    }

    protected int getZZLexicalState() {
        return zzLexicalState;
    }

    protected void popState() {
		yybegin(stack.popStack());
	}

	protected void pushState(final int state) {
		stack.pushStack(getZZLexicalState());
		yybegin(state);
	}
%}



IDENTIFIER=[[:letter:]_\x7f-\xff][[:letter:][:digit:]_\x7f-\xff\.]*"!"?
KEYWORD=("true" | "TRUE" | "false" | "FALSE" | "yes" | "YES" | "no" | "NO" | "null" | "NULL")
WHITESPACE=[ \t]+
NEWLINE=("\r"|"\n"|"\r\n")
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
LITERAL=([^#%\"',=\[\]\{\}\(\)\<\>\t\n\r@ ])+
ARRAY_CLOSE_DELIM = ("]" | "}" | ")")
ARRAY_MINUS_DELIM="-"
ARRAY_ITEM_DELIM=","
D_STRING="\""([^"\r""\n""\r\n""\""]|"\\\"")*"\""
S_STRING="'"([^"\r""\n""\r\n""'"]|"\\'")*"'"
STRING = {D_STRING} | {S_STRING}
VARIABLE="%"{LITERAL}"%"?
ARRAY_KEY=({REFERENCE} | {LITERAL} | {STRING} | {NUMBER}){WHITESPACE}*(":"|"=")
ARRAY_VALUE={WHITESPACE}*({REFERENCE} | {LITERAL} | {STRING} | {NUMBER} | {VARIABLE} | {KEYWORD}){WHITESPACE}*
BLOCK_HEADER={IDENTIFIER}({WHITESPACE}*"<"{WHITESPACE}*{IDENTIFIER})?{WHITESPACE}*":"{WHITESPACE}*({NEWLINE} | {COMMENT})
COMMENT="#"[^"\r""\n""\r\n")]*
BLOCK_ARRAY_SEPARATOR=":" | "="
REFERENCE="@"({IDENTIFIER} | "\\")+

%state ST_IN_BLOCK
%state ST_BLOCK_HEADER
%state ST_VALUED_BLOCK
%state ST_IN_INHERITED_BLOCK
%state ST_IN_RIGHT_BLOCK
%state ST_IN_ARRAY_KEY
%state ST_IN_ARRAY_VALUE
%state ST_IN_MINUS_ARRAY_VALUE
%state ST_IN_SQ_ARRAY
%state ST_IN_CU_ARRAY
%state ST_IN_PA_ARRAY
%state ST_HIGHLIGHTING_ERROR


%%
<YYINITIAL>.|{NEWLINE} {
    yypushback(yylength());
    pushState(ST_IN_BLOCK);
}

<ST_IN_BLOCK, ST_IN_INHERITED_BLOCK, ST_IN_RIGHT_BLOCK, ST_IN_ARRAY_KEY, ST_IN_ARRAY_VALUE, ST_IN_MINUS_ARRAY_VALUE, ST_IN_SQ_ARRAY, ST_IN_CU_ARRAY, ST_IN_PA_ARRAY, ST_BLOCK_HEADER, ST_VALUED_BLOCK>{WHITESPACE}+ {
    return NEON_WHITESPACE;
}

<ST_IN_BLOCK, ST_IN_INHERITED_BLOCK, ST_IN_RIGHT_BLOCK, ST_IN_ARRAY_KEY, ST_IN_ARRAY_VALUE, ST_IN_MINUS_ARRAY_VALUE, ST_IN_SQ_ARRAY, ST_IN_CU_ARRAY, ST_IN_PA_ARRAY, ST_BLOCK_HEADER, ST_VALUED_BLOCK>{COMMENT} {
    return NEON_COMMENT;
}

<ST_IN_BLOCK> {
    {BLOCK_HEADER} {
        pushState(ST_BLOCK_HEADER);
        yypushback(yylength());
    }
    {NEWLINE} {
        return NEON_EOL;
    }
    {BLOCK_ARRAY_SEPARATOR} {
        pushState(ST_IN_RIGHT_BLOCK);
        return NEON_INTERPUNCTION;
    }
    . {
        pushState(ST_VALUED_BLOCK);
        yypushback(yylength());
    }
}

<ST_BLOCK_HEADER, ST_VALUED_BLOCK> {
    {BLOCK_ARRAY_SEPARATOR} {
        popState();
        yypushback(yylength());
    }
    "<" {
        pushState(ST_IN_INHERITED_BLOCK);
        return NEON_INTERPUNCTION;
    }
    {ARRAY_MINUS_DELIM} / {WHITESPACE}+ {
        pushState(ST_IN_MINUS_ARRAY_VALUE);
        return NEON_INTERPUNCTION;
    }
    {NEWLINE} {
        popState();
        yypushback(yylength());
    }
}

<ST_BLOCK_HEADER> {
    {IDENTIFIER} {
        return NEON_BLOCK;
    }
}

<ST_VALUED_BLOCK> {
    {IDENTIFIER} | {STRING} {
        return NEON_VALUED_BLOCK;
    }
}

<ST_IN_INHERITED_BLOCK> {
    {BLOCK_ARRAY_SEPARATOR} {
        popState();
        yypushback(yylength());
    }
    {IDENTIFIER} {
        return NEON_BLOCK;
    }
    {NEWLINE} {
        popState();
        return NEON_WHITESPACE;
    }
}
<ST_IN_RIGHT_BLOCK, ST_IN_ARRAY_VALUE, ST_IN_MINUS_ARRAY_VALUE> {
    {REFERENCE} {
        return NEON_REFERENCE;
    }
    {KEYWORD} {
        return NEON_KEYWORD;
    }
    {NUMBER} {
        return NEON_NUMBER;
    }
    {LITERAL} {
        return NEON_LITERAL;
    }
    {VARIABLE} {
        return NEON_VARIABLE;
    }
    {STRING} {
        return NEON_STRING;
    }
}
<ST_IN_RIGHT_BLOCK, ST_IN_ARRAY_VALUE, ST_IN_MINUS_ARRAY_VALUE> {
    "[" {
        pushState(ST_IN_SQ_ARRAY);
        return NEON_INTERPUNCTION;
    }
    "{" {
        pushState(ST_IN_CU_ARRAY);
        return NEON_INTERPUNCTION;
    }
    "(" {
        pushState(ST_IN_PA_ARRAY);
        return NEON_INTERPUNCTION;
    }
}

<ST_IN_RIGHT_BLOCK, ST_IN_MINUS_ARRAY_VALUE> {
    {NEWLINE} {
        yypushback(yylength());
        popState();
    }
}

<ST_IN_SQ_ARRAY, ST_IN_CU_ARRAY, ST_IN_PA_ARRAY> {
    {ARRAY_KEY} {
        pushState(ST_IN_ARRAY_KEY);
        yypushback(yylength());
    }
    {ARRAY_VALUE} {
        pushState(ST_IN_ARRAY_VALUE);
        yypushback(yylength());
    }
    {ARRAY_ITEM_DELIM} {
        return NEON_INTERPUNCTION;
    }
}

<ST_IN_SQ_ARRAY> {
    "]" {
        popState();
        return NEON_INTERPUNCTION;
    }
}

<ST_IN_CU_ARRAY> {
    "}" {
        popState();
        return NEON_INTERPUNCTION;
    }
}

<ST_IN_PA_ARRAY> {
    ")" {
        popState();
        return NEON_INTERPUNCTION;
    }
}

<ST_IN_ARRAY_KEY> {
    {REFERENCE} {
        return NEON_REFERENCE;
    }
    {NUMBER} {
        return NEON_NUMBER;
    }
    {LITERAL} {
        return NEON_LITERAL;
    }
    {STRING} {
        return NEON_STRING;
    }
    {BLOCK_ARRAY_SEPARATOR} {
        popState();
        return NEON_INTERPUNCTION;
    }
}

<ST_IN_ARRAY_VALUE> {
    {ARRAY_CLOSE_DELIM} | {ARRAY_ITEM_DELIM} {
        popState();
        yypushback(yylength());
    }
}

/* ============================================
   Stay in this state until we find a whitespace.
   After we find a whitespace we go the the prev state and try again from the next token.
   ============================================ */
<ST_HIGHLIGHTING_ERROR> {
	{WHITESPACE} {
        popState();
        return NEON_WHITESPACE;
    }
    . | {NEWLINE} {
        return NEON_UNKNOWN;
    }
}

/* ============================================
   This rule must be the last in the section!!
   it should contain all the states.
   ============================================ */
<YYINITIAL, ST_IN_BLOCK, ST_IN_INHERITED_BLOCK, ST_IN_RIGHT_BLOCK, ST_IN_ARRAY_KEY, ST_IN_ARRAY_VALUE, ST_IN_MINUS_ARRAY_VALUE, ST_IN_SQ_ARRAY, ST_IN_CU_ARRAY, ST_IN_PA_ARRAY, ST_BLOCK_HEADER, ST_VALUED_BLOCK> {
    . {
        yypushback(yylength());
        pushState(ST_HIGHLIGHTING_ERROR);
    }
    {NEWLINE} {
        yypushback(yylength());
        pushState(ST_HIGHLIGHTING_ERROR);
    }
}

{WHITESPACE} { return NEON_WHITESPACE; }
.            { return NEON_UNKNOWN; }
