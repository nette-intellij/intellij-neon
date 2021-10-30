package cz.juzna.intellij.neon.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.lexer.NeonLexer3;
import cz.juzna.intellij.neon.lexer.NeonTypes;
import cz.juzna.intellij.neon.psi.impl.elements.*;
import org.jetbrains.annotations.NotNull;

public class NeonParser2Definition implements ParserDefinition {
	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		return new NeonLexer3();
	}

	@Override
	public PsiParser createParser(Project project) {
		return new NeonParser2();
	}

	@Override
	public IFileElementType getFileNodeType() {
		return NeonElementTypes.FILE;
	}

	@NotNull
	@Override
	public TokenSet getWhitespaceTokens() {
		return NeonTypes.WHITESPACES;
	}

	@NotNull
	@Override
	public TokenSet getCommentTokens() {
		return NeonTypes.COMMENTS;
	}

	@NotNull
	@Override
	public TokenSet getStringLiteralElements() {
		return NeonTypes.STRING_LITERALS;
	}

	@NotNull
	@Override
	public PsiElement createElement(ASTNode node) {
		return NeonTypes.createElement(node);
	}

	@Override
	public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
		return new NeonFileImpl(viewProvider);
	}

	@Override
	public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode astNode, ASTNode astNode1) {
		return SpaceRequirements.MAY;
	}
}
