package cz.juzna.intellij.neon.lexer;


import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.psi.tree.TokenSet;

import java.io.Reader;


public class NeonLexer3 extends MergingLexerAdapter {
	// To be merged
	private static final TokenSet TOKENS_TO_MERGE = TokenSet.create(
			NeonTypes.T_COMMENT,
			NeonTypes.T_WHITESPACE
	);

	public NeonLexer3() {
		super(new FlexAdapter(new _NeonLexer2((Reader) null)), TOKENS_TO_MERGE);
	}
}
