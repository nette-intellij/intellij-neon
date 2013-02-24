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
	private PsiBuilder myBuilder;
	private boolean eolSeen = false;
	private int myIndent;
	private boolean myHasTabs = false; // FIXME: use this
	private PsiBuilder.Marker myAfterLastEolMarker;
	private int myInline;


	@NotNull
	@Override
	public ASTNode parse(IElementType root, PsiBuilder builder) {
		builder.setDebugMode(true);

		myBuilder = builder;
		myIndent = 0;
		eolSeen = false;

		// begin
		PsiBuilder.Marker fileMarker = mark();

		passEmpty(); // process beginning of file
		parseValue(0);
		assert (this.myBuilder.eof()) : "Not all tokens were passed.";

		// end
		fileMarker.done(root);
		return builder.getTreeBuilt();
	}

	private void parseValue(int indent) {
		IElementType currentToken = myBuilder.getTokenType();
		IElementType nextToken = myBuilder.lookAhead(1);

		if (NeonTokenTypes.STRING_LITERALS.contains(currentToken) && nextToken == NEON_COLON || currentToken == NeonTokenTypes.NEON_ARRAY_BULLET) {
			parseArray(indent);
		}

		else if (NeonTokenTypes.STRING_LITERALS.contains(currentToken)) {
			PsiBuilder.Marker val = mark();
			advanceLexer();

			if (myBuilder.getTokenType() == NEON_LPAREN) {
//				PsiBuilder.Marker entity = val.precede();
//				val.done(SCALAR_VALUE);
				parseArguments();
//				entity.done(ENTITY);
				val.done(ENTITY);
			} else {
				val.done(SCALAR_VALUE);
			}
		} else {
			parseCompoundValue(indent);
		}
	}

	private void parseKeyVal(int indent) {
		assert NeonTokenTypes.STRING_LITERALS.contains(myBuilder.getTokenType()) : "Expected literal or string";

		PsiBuilder.Marker keyValPair = mark();
		parseKey();
		eolSeen = false;

		// key colon value
		assert myBuilder.getTokenType() == NEON_COLON : "Expected assignment operator";
		advanceLexer();

		// value
		if (myBuilder.getTokenType() == NEON_INDENT) {
			advanceLexer(); // read indent
//			PsiBuilder.Marker val = mark();
			parseArray(myIndent);
//			val.done(COMPOUND_VALUE);
		} else {
			parseValue(indent);
		}

		keyValPair.done(KEY_VALUE_PAIR);
	}

	private void parseKey() {
		assert NeonTokenTypes.STRING_LITERALS.contains(myBuilder.getTokenType()) : "Expected literal or string";

		PsiBuilder.Marker key = mark();
		advanceLexer();
		key.done(KEY);
	}

	private void parseCompoundValue(int indent) {
		IElementType type = myBuilder.getTokenType();

		advanceLexer();
	}

	private void parseArray(int indent) {
		PsiBuilder.Marker marker = mark();

		while (myIndent >= indent && myBuilder.getTokenType() != null) {
			if (myBuilder.getTokenType() == NEON_ARRAY_BULLET) {
				PsiBuilder.Marker markItem = mark();
				advanceLexer();
				parseValue(indent + 1);
				markItem.done(NeonElementTypes.ITEM);
			} else {
				parseKeyVal(indent + 1);
			}

			if (myBuilder.getTokenType() == NEON_INDENT) advanceLexer();
		}

		marker.done(ARRAY);
	}

	// arguments in parenthesis
	private void parseArguments() {
		assert myBuilder.getTokenType() == NEON_LPAREN : "Expected left parenthesis";

		advanceLexer();

		myInline++;
		PsiBuilder.Marker marker = mark();
		while (myBuilder.getTokenType() != NEON_RPAREN) {
			parseValue(0);
			if (myBuilder.getTokenType() == NEON_ITEM_DELIMITER) advanceLexer();
		}
		marker.done(ARGS);
		myInline--;

		advanceLexer();
	}



	/***  helpers ***/

	private void advanceLexer() {
		if (this.myBuilder.eof()) return;

		IElementType type = this.myBuilder.getTokenType();
		this.eolSeen = this.eolSeen || type == NEON_INDENT;
		if (type == NEON_INDENT) {
//			this.myAfterLastEolMarker = mark();
			this.myIndent = this.myBuilder.getTokenText().length() - 1;
		}
		else {
//			dropEolMarker();
		}
		this.myBuilder.advanceLexer();
	}

	private void dropEolMarker() {
		if (this.myAfterLastEolMarker != null) {
			this.myAfterLastEolMarker.drop();
			this.myAfterLastEolMarker = null;
		}
	}

	private void rollBackToEol() {
		if ((this.eolSeen) && (this.myAfterLastEolMarker != null)) {
			this.eolSeen = false;
			this.myAfterLastEolMarker.rollbackTo();
			this.myAfterLastEolMarker = null;
		}
	}

	private PsiBuilder.Marker mark() {
		dropEolMarker();
		return this.myBuilder.mark();
	}

	private void passEmpty() {
		while (!myBuilder.eof() && (myBuilder.getTokenType() == NEON_INDENT || myBuilder.getTokenType() == NEON_UNKNOWN)) {
			advanceLexer();
		}
	}
}
