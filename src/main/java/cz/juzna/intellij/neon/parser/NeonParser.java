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
		parseStatements(0);
		assert (this.myBuilder.eof()) : "Not all tokens were passed.";

		// end
		fileMarker.done(root);
		return builder.getTreeBuilt();
	}


	private void parseStatements(int indent) {
		while (!myBuilder.eof() && (myIndent >= indent)) {
			parseStatement(indent);
		}
	}

	private void parseStatement(int indent) {
		if (myBuilder.eof()) return;

		IElementType type = myBuilder.getTokenType();
		if(type == NEON_KEY) parseKeyVal(indent);
		else parseValue(indent);

		passEmpty();
	}


	// arguments in parenthesis
	private void parseArguments() {
		assert myBuilder.getTokenType() == NEON_LPAREN : "Expected left parenthesis";

		PsiBuilder.Marker marker = mark();
		advanceLexer();
		while (myBuilder.getTokenType() != NEON_RPAREN) {
			parseValue(0);
			if (myBuilder.getTokenType() == NEON_ITEM_DELIMITER) advanceLexer();
		}
		advanceLexer();
		marker.done(ARGS);
	}

	private void parseHash() {
		assert myBuilder.getTokenType() == NEON_LBRACE_CURLY : "Expected left curly bracket";

		PsiBuilder.Marker marker = mark();
		advanceLexer();
		while (myBuilder.getTokenType() != NEON_RBRACE_CURLY) {
			parseKeyVal(myIndent);
			if (myBuilder.getTokenType() == NEON_ITEM_DELIMITER) advanceLexer();
		}
		advanceLexer();
		marker.done(HASH);
	}

	private void parseArray() {
		assert myBuilder.getTokenType() == NEON_LBRACE_CURLY : "Expected left square bracket";

		PsiBuilder.Marker marker = mark();
		advanceLexer();
		while (myBuilder.getTokenType() != NEON_RBRACE_SQUARE) {
			parseValue(0);
			if (myBuilder.getTokenType() == NEON_ITEM_DELIMITER) advanceLexer();
		}
		advanceLexer();
		marker.done(ARRAY);
	}

	private void parseKeyVal(int indent) {
		assert myBuilder.getTokenType() == NEON_KEY : "Expected key";

		PsiBuilder.Marker keyValPair = mark();
		parseKey();
		eolSeen = false;

		// key colon value
		assert myBuilder.getTokenType() == NEON_ASSIGNMENT : "Expected assignment operator";
		advanceLexer();

		// value
		if (myBuilder.getTokenType() == NEON_INDENT) {
			advanceLexer(); // read indent
			PsiBuilder.Marker val = mark();
			parseStatements(myIndent);
			val.done(COMPOUND_VALUE);
		} else {
			parseValue(indent);
		}

		keyValPair.done(KEY_VALUE_PAIR);
	}

	private void parseKey() {
		assert myBuilder.getTokenType() == NEON_KEY : "Expected key";
		PsiBuilder.Marker key = mark();
		advanceLexer();

		if (myBuilder.getTokenType() == NEON_BLOCK_INHERITENCE) {
			PsiBuilder.Marker compound = key.precede();
			key.done(KEY);

			advanceLexer();
			key = mark();
			advanceLexer();
			key.done(KEY);
			compound.done(COMPOUND_KEY);

		} else {
			key.done(KEY);
		}
	}

	private void parseSequence(int indent) {
		assert myBuilder.getTokenType() == NEON_ARRAY_BULLET : "Expected array bullet";
		PsiBuilder.Marker sequence = mark();

		while (myIndent >= indent && myBuilder.getTokenType() == NEON_ARRAY_BULLET) {
			PsiBuilder.Marker item = mark();
			advanceLexer();
			parseStatement(indent + 1);
			passEmpty();
			item.done(ITEM);
		}

		sequence.done(SEQUENCE);
	}


	private void parseValue(int indent) {
		IElementType type = myBuilder.getTokenType();

		if (type == NEON_REFERENCE) {
			PsiBuilder.Marker val = mark();
			advanceLexer();
			val.done(REFERENCE);

		} else if (SCALAR_VALUES.contains(type)) {
			PsiBuilder.Marker val = mark();
			advanceLexer();

			if (myBuilder.getTokenType() == NEON_LPAREN) {
				parseArguments();
				val.done(ENTITY);
			} else {
				val.done(SCALAR_VALUE);
			}
		} else {
			parseCompoundValue(indent);
		}
	}

	private void parseCompoundValue(int indent) {
		IElementType type = myBuilder.getTokenType();

		if(type == NEON_LBRACE_SQUARE) parseArray();
		else if(type == NEON_LBRACE_CURLY) parseHash();
		else if(type == NEON_ARRAY_BULLET) parseSequence(myIndent);
		else advanceLexer(); // not known
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
