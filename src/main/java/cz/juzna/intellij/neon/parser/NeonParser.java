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
		while (! this.myBuilder.eof()) {
			if (myBuilder.getTokenType() != NEON_INDENT) {
				myBuilder.error("unexpected token at end of file");
			}
			myBuilder.advanceLexer();
		}

		// end
		fileMarker.done(root);
		return builder.getTreeBuilt();
	}

	private void parseValue(int indent) {
		IElementType currentToken = myBuilder.getTokenType();
		IElementType nextToken = myBuilder.lookAhead(1);

		if (NeonTokenTypes.STRING_LITERALS.contains(currentToken) && nextToken == NEON_COLON || currentToken == NeonTokenTypes.NEON_ARRAY_BULLET) {
			PsiBuilder.Marker val = mark();
			parseArray(indent);
			val.done(ARRAY);
		}

		else if (NeonTokenTypes.STRING_LITERALS.contains(currentToken)) {
			PsiBuilder.Marker val = mark();
			advanceLexer();

			if (myBuilder.getTokenType() == NEON_LPAREN) {
				myInline++;

				PsiBuilder.Marker valArray = mark();

				advanceLexer(NEON_LPAREN); // opening bracket
				parseArray(1000000);
				advanceLexer(NEON_RPAREN); // closing bracket

				valArray.done(ARRAY);

				myInline--;

				val.done(ENTITY);

			} else {
				val.done(SCALAR_VALUE);
			}

		} else if (OPEN_BRACKET.contains(currentToken)) { // array
			PsiBuilder.Marker val = mark();
			myInline++;

			IElementType closing = closingBrackets.get(currentToken);

			advanceLexer(); // opening bracket
			parseArray(1000000);
			advanceLexer(closing); // closing bracket

			myInline--;

			val.done(ARRAY);

		} else if(currentToken == NEON_INDENT) {
			// no value -> null

		} else {
			// dunno
			myBuilder.error("unexpected token " + currentToken);
			advanceLexer();
		}
	}

	private void parseArray(int indent) {
//		PsiBuilder.Marker marker = mark();
		boolean isInline = myInline > 0;

		while (myBuilder.getTokenType() != null && ! CLOSING_BRACKET.contains(myBuilder.getTokenType()) && (isInline ? myInline > 0 : myIndent >= indent)) {
			IElementType currentToken = myBuilder.getTokenType();
			IElementType nextToken = myBuilder.lookAhead(1);

			if (ASSIGNMENTS.contains(nextToken)) { // key-val pair
				parseKeyVal(indent);

			} else if (currentToken == NEON_ARRAY_BULLET) {
				PsiBuilder.Marker markItem = mark();
				advanceLexer();
				parseValue(indent + 1);
				markItem.done(NeonElementTypes.ITEM);

			} else if(isInline) {
				parseValue(indent);

			} else {
				myBuilder.error("expected key-val pair or array item");
				advanceLexer();

			}

			if (myBuilder.getTokenType() == NEON_INDENT) advanceLexer();
		}

//		marker.done(ARRAY);
	}

	private void parseKeyVal(int indent) {
		myAssert(NeonTokenTypes.STRING_LITERALS.contains(myBuilder.getTokenType()), "Expected literal or string");

		PsiBuilder.Marker keyValPair = mark();
		parseKey();
		eolSeen = false;

		// key colon value
		myAssert(ASSIGNMENTS.contains(myBuilder.getTokenType()), "Expected assignment operator");
		advanceLexer();

		// value
		if (myBuilder.getTokenType() == NEON_INDENT) {
			advanceLexer(); // read indent
			if (myIndent > indent) {
				PsiBuilder.Marker val = mark();
				parseArray(myIndent);
				val.done(ARRAY);
			} else {
				// myBuilder.error("value missing"); // actually not an error, but null
			}
		} else {
			parseValue(indent);
		}

		keyValPair.done(KEY_VALUE_PAIR);
	}

	private void parseKey() {
		myAssert(NeonTokenTypes.STRING_LITERALS.contains(myBuilder.getTokenType()), "Expected literal or string");

		PsiBuilder.Marker key = mark();
		advanceLexer();
		key.done(KEY);
	}


	/***  helpers ***/

	/**
	 * Go to next token; if there is more whitespace, skip to the last
	 */
	private void advanceLexer() {
		if (this.myBuilder.eof()) return;

		do {
			IElementType type = myBuilder.getTokenType();
			this.eolSeen = this.eolSeen || type == NEON_INDENT;
			if (type == NEON_INDENT) {
				myIndent = myBuilder.getTokenText().length() - 1;
			}

			myBuilder.advanceLexer();
		} while (myBuilder.getTokenType() == NEON_INDENT && myBuilder.lookAhead(1) == NEON_INDENT); // keep going if we're still indented
	}

	private void advanceLexer(IElementType expectedToken) {
		if (myBuilder.getTokenType() == expectedToken) {
			advanceLexer();

		} else {
			myBuilder.error("unexpected token " + myBuilder.getTokenType() + ", expected " + expectedToken);

		}
	}

	private void myAssert(boolean condition, String message) {
		if ( ! condition) {
			myBuilder.error(message + ", got " + myBuilder.getTokenType());
			advanceLexer();
		}
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
