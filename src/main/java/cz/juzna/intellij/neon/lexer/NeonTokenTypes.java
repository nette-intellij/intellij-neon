package cz.juzna.intellij.neon.lexer;

import com.intellij.lang.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILightStubFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.diff.FlyweightCapableTreeStructure;
import cz.juzna.intellij.neon.NeonLanguage;
import cz.juzna.intellij.neon.indexes.stubs.NeonFileStub;
import cz.juzna.intellij.neon.parser.NeonParser;

/**
 * Types of tokens returned form lexer
 *
 * @author Jan Dolecek - juzna.cz@gmail.com
 */
public interface NeonTokenTypes extends _NeonTokenTypes
{
	ILightStubFileElementType<NeonFileStub> FILE = new ILightStubFileElementType<NeonFileStub>(NeonLanguage.INSTANCE)  {
		@Override
		public FlyweightCapableTreeStructure<LighterASTNode> parseContentsLight(ASTNode chameleon) {
			PsiElement psi = chameleon.getPsi();
			assert psi != null : "Bad chameleon: " + chameleon;

			Project project = psi.getProject();
			PsiBuilderFactory factory = PsiBuilderFactory.getInstance();
			PsiBuilder builder = factory.createBuilder(project, chameleon);
			ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(getLanguage());
			assert parserDefinition != null : this;

			NeonParser parser = new NeonParser();
			parser.parseLight(this, builder);
			return builder.getLightTree();
		}
	};

	IElementType NEON_SYMBOL = new NeonTokenType("symbol"); // use a symbol or brace instead (see below)

	// special tokens (identifier in block header or as array key)
	IElementType NEON_KEYWORD = new NeonTokenType("keyword");
	IElementType NEON_KEY = new NeonTokenType("key");
	IElementType NEON_CLASS_REFERENCE = new NeonTokenType("class reference");

	// sets
	TokenSet WHITESPACES = TokenSet.create(NEON_WHITESPACE, TokenType.WHITE_SPACE);
	TokenSet COMMENTS = TokenSet.create(NEON_COMMENT);
	TokenSet KEY_LITERALS = TokenSet.create(
			NEON_LITERAL, NEON_IDENTIFIER, NEON_CONCATENATION, NEON_METHOD, NEON_DATE_TIME
	);
	TokenSet STRING_LITERALS = TokenSet.create(
			NEON_LITERAL, NEON_STRING, NEON_NUMBER, NEON_METHOD, NEON_DOUBLE_COLON,
			NEON_KEY_IDENTIFIER, NEON_PHP_STATIC_IDENTIFIER, NEON_KEY_USAGE, NEON_PARAMETER_USAGE,
			NEON_IDENTIFIER
	);
	TokenSet STRING_QUOTES = TokenSet.create(
			NEON_SINGLE_QUOTE_LEFT, NEON_DOUBLE_QUOTE_LEFT,
			NEON_SINGLE_QUOTE_RIGHT, NEON_DOUBLE_QUOTE_RIGHT
	);
	TokenSet HIGHLIGHT_KEYWORD_ELEMENTS = TokenSet.create(
			NEON_NAMESPACE_REFERENCE, NEON_NAMESPACE_RESOLUTION, NEON_IDENTIFIER, NEON_CONCATENATION,
			NEON_ARRAY_BULLET, NEON_ASSIGNMENT, NEON_DOUBLE_COLON, NEON_ITEM_DELIMITER, NEON_NUMBER,
			NEON_DATE_TIME, NEON_LITERAL
	);
	TokenSet KEY_VAL_PAIRS = TokenSet.create(
			KEY_VAL_PAIR, ARRAY_KEY_VALUE_PAIR
	);
	TokenSet ARRAYS = TokenSet.create(
			ARRAY, MAIN_ARRAY
	);
	TokenSet CLASS_USAGE_ELEMENTS = TokenSet.create(
			NEON_NAMESPACE_REFERENCE, NEON_NAMESPACE_RESOLUTION, NEON_IDENTIFIER, NEON_METHOD
	);

}
