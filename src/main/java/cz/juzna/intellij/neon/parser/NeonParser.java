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
	private String myIndentString;


	@NotNull
	@Override
	public ASTNode parse(IElementType root, PsiBuilder builder) {
		builder.setDebugMode(true);

		myBuilder = builder;
		myIndent = 0;
		eolSeen = false;
		myIndentString = "";

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
		} else if (currentToken == NEON_INDENT) {
			// no value -> null

		} else if (OPEN_BRACKET.contains(currentToken)) { // array
			PsiBuilder.Marker val = mark();
			parseInlineArray();
			if (myBuilder.getTokenType() == NEON_LPAREN){
				parseEntity(val);
			} else {
				val.drop();
			}
		} else if (NeonTokenTypes.STRING_LITERALS.contains(currentToken)) {
			PsiBuilder.Marker val = mark();
			advanceLexer();
			if (myBuilder.getTokenType() == NEON_LPAREN) {
				parseEntity(val);
			} else {
				val.done(SCALAR_VALUE);
			}
		} else {
			// dunno
			myBuilder.error("unexpected token " + currentToken);
			advanceLexer();
		}
	}

	private void parseArray(int indent, boolean onlyBullets)
	{
//		PsiBuilder.Marker marker = mark();
		boolean isInline = myInline > 0;

		while (myBuilder.getTokenType() != null && ! CLOSING_BRACKET.contains(myBuilder.getTokenType()) && (isInline ? myInline > 0 : myIndent >= indent)) {
			if (!isInline && myIndent != indent) {
				myBuilder.error("bad indent");
			}
			IElementType currentToken = myBuilder.getTokenType();
			if(onlyBullets && currentToken != NEON_ARRAY_BULLET) {
				return;
			}
			IElementType nextToken = myBuilder.lookAhead(1);


			if (currentToken == NEON_ARRAY_BULLET && STRING_LITERALS.contains(nextToken) && ASSIGNMENTS.contains(myBuilder.lookAhead(2))) { //key-after-bullet
				PsiBuilder.Marker markItem = mark();
				advanceLexer();
				PsiBuilder.Marker markArray = mark();
				parseKeyVal(indent);
				String prevIndent = myIndentString;
				advanceLexer();
				if ((prevIndent + "  ").equals(myIndentString)) {
					parseArray(indent + 2);
				}
				markArray.done(ARRAY);
				markItem.done(ITEM);
			} else if (ASSIGNMENTS.contains(nextToken)) { // key-val pair
				parseKeyVal(indent);

			} else if (currentToken == NEON_ARRAY_BULLET) {
				PsiBuilder.Marker markItem = mark();
				advanceLexer();
				parseValueOrArray(indent);
				markItem.done(NeonElementTypes.ITEM);

			} else if(isInline && currentToken == NEON_ITEM_DELIMITER) {
				advanceLexer();
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

	private void parseArray(int indent) {
		parseArray(indent, false);
	}

	private void parseKeyVal(int indent) {
		myAssert(NeonTokenTypes.STRING_LITERALS.contains(myBuilder.getTokenType()), "Expected literal or string");

		PsiBuilder.Marker keyValPair = mark();
		parseKey();
		eolSeen = false;

		// key colon value
		myAssert(ASSIGNMENTS.contains(myBuilder.getTokenType()), "Expected assignment operator");
		advanceLexer();
		if(myBuilder.getTokenType() == NEON_INDENT && myBuilder.lookAhead(1) == NEON_ARRAY_BULLET) {
			//array-after-key syntax
			advanceLexer(); //read indent
			PsiBuilder.Marker val = mark();
			parseArray(myIndent, myIndent == indent);
			val.done(ARRAY);
		} else  if((myBuilder.getTokenType() != NEON_INDENT && myBuilder.getTokenType() != null)
				|| (myBuilder.getTokenType() == NEON_INDENT && (myBuilder.getTokenText().length() - 1) > myIndent)){
			// value
			parseValueOrArray(indent);
		}


		keyValPair.done(KEY_VALUE_PAIR);
	}

	private void parseValueOrArray(int indent) {
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
	}

	private void parseKey() {
		myAssert(NeonTokenTypes.STRING_LITERALS.contains(myBuilder.getTokenType()), "Expected literal or string");

		PsiBuilder.Marker key = mark();
		advanceLexer();
		key.done(KEY);
	}

	private boolean parseInlineArray()
	{
		IElementType currentToken = myBuilder.getTokenType();
		PsiBuilder.Marker val = mark();
		myInline++;

		IElementType closing = closingBrackets.get(currentToken);

		advanceLexer(currentToken); // opening bracket
		parseArray(1000000);
		boolean ok = advanceLexer(closing); // closing bracket
		myInline--;
		val.done(ARRAY);

		return ok;
	}

	private void parseEntity(PsiBuilder.Marker val)
	{
		parseInlineArray();
		if (STRING_LITERALS.contains(myBuilder.getTokenType())) {
			val.rollbackTo();
			val = mark();
			parseChainedEntity();
			val.done(CHAINED_ENTITY);
		} else {
			val.done(ENTITY);
		}
	}

	private void parseChainedEntity() {
		while (true) {
			PsiBuilder.Marker inlineEntity = mark();
			advanceLexer();
			parseInlineArray();
			inlineEntity.done(ENTITY);
			if (myBuilder.getTokenType() == NEON_INDENT || myBuilder.getTokenType() == null) {
				return;
			} else if (myBuilder.lookAhead(1) != NEON_LPAREN) {
				//last entity without attributes
				PsiBuilder.Marker inlineEntity2 = mark();
				advanceLexer();
				inlineEntity2.done(ENTITY);
				return;
			}
		}
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
				validateTabsSpaces();
				myIndent = myBuilder.getTokenText().length() - 1;
			}

			myBuilder.advanceLexer();
		} while (myBuilder.getTokenType() == NEON_INDENT && myBuilder.lookAhead(1) == NEON_INDENT); // keep going if we're still indented
	}

	private boolean advanceLexer(IElementType expectedToken) {
		if (myBuilder.getTokenType() == expectedToken) {
			advanceLexer();
			return true;

		} else {
			myBuilder.error("unexpected token " + myBuilder.getTokenType() + ", expected " + expectedToken);
			return false;
		}
	}

	/**
	 * Check that only tabs or only spaces are used for indent
	 */
	private void validateTabsSpaces() {
		assert myBuilder.getTokenType() == NEON_INDENT;
		String text = myBuilder.getTokenText().replace("\n", "");
		if (text.isEmpty() && myBuilder.lookAhead(1) == NEON_INDENT) {
			return;
		}
		if (text.isEmpty()) {
			myIndentString = "";
			return;
		}
		int min = Math.min(myIndentString.length(), text.length());
		if (!text.substring(0, min).equals(myIndentString.substring(0, min))) {
			myBuilder.error("tab/space mixing");
		}
		myIndentString = text;
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
