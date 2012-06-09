package cz.juzna.intellij.neon.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Neon parser, convert tokens (output from lexer) into syntax tree
 */
public class NeonParser implements PsiParser, NeonTokenTypes, NeonElementTypes {
	@NotNull
	@Override
	public ASTNode parse(IElementType root, PsiBuilder builder) {
		final PsiBuilder.Marker rootMarker = builder.mark();

		while (!builder.eof()) {
			IElementType token = builder.getTokenType();

			if (token != null) {
				builder.mark().done(token);
			}

			builder.advanceLexer();
		}

		rootMarker.done(root);
		return builder.getTreeBuilt();
	}

}
