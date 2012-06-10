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
		else if (type == NEON_LPAREN) parseArguments();
		else if(type == NEON_LBRACE_SQUARE) parseArray();
		else if(type == NEON_LBRACE_CURLY) parseHash();
		else advanceLexer(); // not known

		passEmpty();
	}


	// arguments in parenthesis
	private void parseArguments() {
		PsiBuilder.Marker marker = mark();
		while (myBuilder.getTokenType() != NEON_RPAREN) {
			// TODO: recognize
			advanceLexer();
		}
		marker.done(ARGS);
	}

	private void parseHash() {
		PsiBuilder.Marker marker = mark();
		while (myBuilder.getTokenType() != NEON_RBRACE_CURLY) {
			parseKeyVal(myIndent);
			passEmpty();
		}
		advanceLexer();
		marker.done(HASH);
	}

	private void parseArray() {
		PsiBuilder.Marker marker = mark();
		while (myBuilder.getTokenType() != NEON_RBRACE_SQUARE) {
			parseValue();
			passEmpty();
		}
		advanceLexer();
		marker.done(SEQUENCE);
	}

	private void parseKeyVal(int indent) {
		assert myBuilder.getTokenType() == NEON_KEY;

		PsiBuilder.Marker keyValPair = mark();
		eolSeen = false;

		// key colon value
		advanceLexer();
		assert myBuilder.getTokenType() == NEON_ASSIGNMENT;

		advanceLexer(); passEmpty();
		parseValue();

		keyValPair.done(KEY_VALUE_PAIR);
	}

	private void parseValue() {
		if (SCALAR_VALUES.contains(myBuilder.getTokenType())) {
			PsiBuilder.Marker val = mark();
			advanceLexer();
			val.done(SCALAR_TEXT_VALUE);
		} else {
			PsiBuilder.Marker val = mark();
			parseStatements(myIndent);
			val.done(COMPOUND_VALUE);
		}
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
		while (!myBuilder.eof() && (myBuilder.getTokenType() == NEON_INDENT || myBuilder.getTokenType() == NEON_WHITESPACE)) {
			advanceLexer();
		}
	}
}
