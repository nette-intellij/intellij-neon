package cz.juzna.intellij.neon.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static cz.juzna.intellij.neon.lexer._NeonTokenTypes.*;

%%

%{
  public _NeonLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _NeonLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+


%%
<YYINITIAL> {
  {WHITE_SPACE}                   { return WHITE_SPACE; }

  "NEON_INDENT"                   { return NEON_INDENT; }
  "NEON_WHITESPACE"               { return NEON_WHITESPACE; }
  "NEON_LBRACE_SQUARE"            { return NEON_LBRACE_SQUARE; }
  "NEON_RBRACE_SQUARE"            { return NEON_RBRACE_SQUARE; }
  "NEON_LBRACE_CURLY"             { return NEON_LBRACE_CURLY; }
  "NEON_RBRACE_CURLY"             { return NEON_RBRACE_CURLY; }
  "NEON_LPAREN"                   { return NEON_LPAREN; }
  "NEON_RPAREN"                   { return NEON_RPAREN; }
  "NEON_ITEM_DELIMITER"           { return NEON_ITEM_DELIMITER; }
  "NEON_COLON"                    { return NEON_COLON; }
  "NEON_ARRAY_BULLET"             { return NEON_ARRAY_BULLET; }
  "NEON_CONCATENATION"            { return NEON_CONCATENATION; }
  "NEON_LITERAL"                  { return NEON_LITERAL; }
  "NEON_IDENTIFIER"               { return NEON_IDENTIFIER; }
  "NEON_ASSIGNMENT"               { return NEON_ASSIGNMENT; }
  "NEON_DOUBLE_COLON"             { return NEON_DOUBLE_COLON; }
  "NEON_NUMBER"                   { return NEON_NUMBER; }
  "NEON_DATE_TIME"                { return NEON_DATE_TIME; }
  "NEON_PHP_STATIC_IDENTIFIER"    { return NEON_PHP_STATIC_IDENTIFIER; }
  "NEON_KEY_IDENTIFIER"           { return NEON_KEY_IDENTIFIER; }
  "NEON_UNKNOWN"                  { return NEON_UNKNOWN; }
  "NEON_COMMENT"                  { return NEON_COMMENT; }
  "NEON_STRING"                   { return NEON_STRING; }
  "NEON_NAMESPACE_RESOLUTION"     { return NEON_NAMESPACE_RESOLUTION; }
  "NEON_METHOD"                   { return NEON_METHOD; }
  "NEON_NAMESPACE_REFERENCE"      { return NEON_NAMESPACE_REFERENCE; }
  "NEON_KEY_USAGE"                { return NEON_KEY_USAGE; }
  "NEON_PARAMETER_LEFT"           { return NEON_PARAMETER_LEFT; }
  "NEON_PARAMETER_DELIMITER"      { return NEON_PARAMETER_DELIMITER; }
  "NEON_PARAMETER_RIGHT"          { return NEON_PARAMETER_RIGHT; }
  "NEON_PARAMETER_USAGE"          { return NEON_PARAMETER_USAGE; }
  "NEON_PHP_STATIC_METHOD"        { return NEON_PHP_STATIC_METHOD; }
  "NEON_SINGLE_QUOTE_LEFT"        { return NEON_SINGLE_QUOTE_LEFT; }
  "NEON_SINGLE_QUOTE_RIGHT"       { return NEON_SINGLE_QUOTE_RIGHT; }
  "NEON_DOUBLE_QUOTE_LEFT"        { return NEON_DOUBLE_QUOTE_LEFT; }
  "NEON_DOUBLE_QUOTE_RIGHT"       { return NEON_DOUBLE_QUOTE_RIGHT; }


}

[^] { return BAD_CHARACTER; }
