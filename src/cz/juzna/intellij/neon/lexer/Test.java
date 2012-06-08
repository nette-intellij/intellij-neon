package cz.juzna.intellij.neon.lexer;


import com.intellij.psi.tree.IElementType;

import java.io.Reader;

public class Test {
    
    public static void main(String[] args) throws Exception {
        String str = "pepa: novak\nfranta:\n - vopicka\n - kralik\n";
        System.out.println(str);

        _NeonLexer lexer = new _NeonLexer((Reader) null);
        IElementType el;

        lexer.reset(str, 0, str.length(), _NeonLexer.YYINITIAL);
 
        while((el = lexer.advance()) != null) {
            System.out.printf("%s: %d %d '%s'\n", el.toString(), lexer.getTokenStart(), lexer.getTokenEnd(), str.substring(lexer.getTokenStart(), lexer.getTokenEnd()));
        }
    }
}
