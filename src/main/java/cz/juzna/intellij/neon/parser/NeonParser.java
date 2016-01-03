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
	private int myIndent;
	private String myIndentString;


	@NotNull
	@Override
	public ASTNode parse(IElementType root, PsiBuilder builder) {
		builder.setDebugMode(true);

		myBuilder = builder;
		myIndent = 0;
		myIndentString = "";

		// begin
		PsiBuilder.Marker fileMarker = myBuilder.mark();

		passEmpty(); // process beginning of file
		parseValueOrArray(0);
		while (!myBuilder.eof()) {
			if (myBuilder.getTokenType() != NEON_INDENT) {
				myBuilder.error("unexpected token at end of file");
			}
			myBuilder.advanceLexer();
		}

		// end
		fileMarker.done(root);
		return builder.getTreeBuilt();
	}

	private void parseValue() {
		IElementType currentToken = myBuilder.getTokenType();

		if (currentToken == NEON_INDENT || currentToken == null) {
			// no value -> null

		} else if (OPEN_BRACKET.contains(currentToken)) { // array
			PsiBuilder.Marker val = myBuilder.mark();
			parseInlineArray();
			if (myBuilder.getTokenType() == NEON_LPAREN) {
				parseEntity(val);
			} else {
				val.drop();
			}
		} else if (NeonTokenTypes.STRING_LITERALS.contains(currentToken)) {
			PsiBuilder.Marker val = myBuilder.mark();
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

	private void parseArray(int indent, boolean onlyBullets) {
		while (myBuilder.getTokenType() != null && !CLOSING_BRACKET.contains(myBuilder.getTokenType()) && myIndent >= indent) {
			if (myIndent != indent) {
				myBuilder.error("bad indent");
			}
			IElementType currentToken = myBuilder.getTokenType();
			if (onlyBullets && currentToken != NEON_ARRAY_BULLET) {
				return;
			}
			IElementType nextToken = myBuilder.lookAhead(1);


			if (currentToken == NEON_ARRAY_BULLET && STRING_LITERALS.contains(nextToken) && ASSIGNMENTS.contains(myBuilder.lookAhead(2))) { //key-after-bullet
				PsiBuilder.Marker markItem = myBuilder.mark();
				advanceLexer();
				PsiBuilder.Marker markArray = myBuilder.mark();
				parseKeyVal(indent);
				String prevIndent = myIndentString;
				passEmpty();
				if ((prevIndent + "  ").equals(myIndentString)) {
					parseArray(indent + 2);
				}
				markArray.done(ARRAY);
				markItem.done(ITEM);
			} else if (ASSIGNMENTS.contains(nextToken)) { // key-val pair
				parseKeyVal(indent);

			} else if (currentToken == NEON_ARRAY_BULLET) {
				PsiBuilder.Marker markItem = myBuilder.mark();
				advanceLexer();
				parseValueOrArray(indent);
				markItem.done(NeonElementTypes.ITEM);
			} else {
				myBuilder.error("expected key-val pair or array item");
				advanceLexer();

			}

			if (myBuilder.getTokenType() == NEON_INDENT) {
				advanceLexer();
			}
		}
	}

	private void parseArray(int indent) {
		parseArray(indent, false);
	}

	private void parseKeyVal(int indent) {
		myAssert(NeonTokenTypes.STRING_LITERALS.contains(myBuilder.getTokenType()), "Expected literal or string");

		PsiBuilder.Marker keyValPair = myBuilder.mark();
		parseKey();

		// key colon value
		myAssert(ASSIGNMENTS.contains(myBuilder.getTokenType()), "Expected assignment operator");
		advanceLexer();
		if (myBuilder.getTokenType() == NEON_INDENT && myBuilder.lookAhead(1) == NEON_ARRAY_BULLET) {
			//array-after-key syntax
			advanceLexer(); //read indent
			PsiBuilder.Marker val = myBuilder.mark();
			parseArray(myIndent, myIndent == indent);
			val.done(ARRAY);
		} else {
			// value
			parseValueOrArray(indent);
		}


		keyValPair.done(KEY_VALUE_PAIR);
	}

	private void parseValueOrArray(int indent) {
		IElementType currentToken = myBuilder.getTokenType();
		if (currentToken == NEON_INDENT) {
			advanceLexer(); // read indent
			if (myIndent > indent) { //null otherwise
				PsiBuilder.Marker val = myBuilder.mark();
				parseArray(myIndent);
				val.done(ARRAY);
			}
		} else if (NeonTokenTypes.STRING_LITERALS.contains(currentToken) && myBuilder.lookAhead(1) == NEON_COLON || currentToken == NeonTokenTypes.NEON_ARRAY_BULLET) {
			PsiBuilder.Marker val = myBuilder.mark();
			parseArray(indent);
			val.done(ARRAY);
		} else if (currentToken != null) {
			parseValue();
		}
	}

	private void parseKey() {
		myAssert(NeonTokenTypes.STRING_LITERALS.contains(myBuilder.getTokenType()), "Expected literal or string");

		PsiBuilder.Marker key = myBuilder.mark();
		advanceLexer();
		key.done(KEY);
	}

	private boolean parseInlineArray() {
		IElementType currentToken = myBuilder.getTokenType();
		PsiBuilder.Marker val = myBuilder.mark();

		IElementType closing = closingBrackets.get(currentToken);

		advanceLexer(currentToken); // opening bracket
		passEmpty();
		while (myBuilder.getTokenType() != null && !CLOSING_BRACKET.contains(myBuilder.getTokenType())) {
			if (STRING_LITERALS.contains(myBuilder.getTokenType()) && ASSIGNMENTS.contains(myBuilder.lookAhead(1))) { // key-val pair
				PsiBuilder.Marker keyValPair = myBuilder.mark();
				parseKey();
				advanceLexer();
				parseValue();
				readInlineArraySeparator();

				keyValPair.done(KEY_VALUE_PAIR);
			} else {
				parseValue();
				readInlineArraySeparator();
			}
			passEmpty();
		}
		boolean ok = advanceLexer(closing); // closing bracket
		val.done(ARRAY);

		return ok;
	}

	private void readInlineArraySeparator() {
		if (myBuilder.getTokenType() == NEON_INDENT || myBuilder.getTokenType() == NEON_ITEM_DELIMITER) {
			advanceLexer();
		} else if (!CLOSING_BRACKET.contains(myBuilder.getTokenType())) {
			myBuilder.error("Expected indent, delimiter or closing bracket");
		}
	}

	private void parseEntity(PsiBuilder.Marker val) {
		parseInlineArray();
		val.done(ENTITY);
		if (STRING_LITERALS.contains(myBuilder.getTokenType())) {
			val = val.precede();
			parseChainedEntity();
			val.done(CHAINED_ENTITY);
		}
	}

	private void parseChainedEntity() {
		while (true) {
			PsiBuilder.Marker inlineEntity = myBuilder.mark();
			advanceLexer();
			if (myBuilder.getTokenType() != NEON_LPAREN) {
				//last entity without attributes
				inlineEntity.done(ENTITY);
				return;
			}
			parseInlineArray();
			inlineEntity.done(ENTITY);
			if (myBuilder.getTokenType() == NEON_INDENT || myBuilder.getTokenType() == null) {
				return;
			}
		}
	}


	/***  helpers ***/

	/**
	 * Go to next token; if there is more whitespace, skip to the last
	 */
	private void advanceLexer() {
		if (myBuilder.eof()) return;

		do {
			IElementType type = myBuilder.getTokenType();
			if (type == NEON_INDENT) {
				validateTabsSpaces();
				myIndent = myBuilder.getTokenText().length() - 1;
			}

			myBuilder.advanceLexer();
		}
		while (myBuilder.getTokenType() == NEON_INDENT && myBuilder.lookAhead(1) == NEON_INDENT); // keep going if we're still indented
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
		if (!condition) {
			myBuilder.error(message + ", got " + myBuilder.getTokenType());
			advanceLexer();
		}
	}

	private void passEmpty() {
		while (!myBuilder.eof() && (myBuilder.getTokenType() == NEON_INDENT || myBuilder.getTokenType() == NEON_UNKNOWN)) {
			advanceLexer();
		}
	}
}
