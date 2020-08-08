package cz.juzna.intellij.neon.editor.action;

import com.intellij.codeInsight.editorActions.QuoteHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;

public class NeonQuoteHandler implements QuoteHandler {

	@Override
	public boolean isClosingQuote(HighlighterIterator iterator, int offset) {
		return iterator.getTokenType() == NeonTokenTypes.NEON_SINGLE_QUOTE_RIGHT || iterator.getTokenType() == NeonTokenTypes.NEON_DOUBLE_QUOTE_RIGHT;
	}

	@Override
	public boolean isOpeningQuote(HighlighterIterator iterator, int offset) {
		return iterator.getTokenType() == NeonTokenTypes.NEON_DOUBLE_QUOTE_LEFT || iterator.getTokenType() == NeonTokenTypes.NEON_DOUBLE_QUOTE_LEFT;
	}

	@Override
	public boolean hasNonClosedLiteral(Editor editor, HighlighterIterator iterator, int offset) {
		return true;
	}

	@Override
	public boolean isInsideLiteral(HighlighterIterator iterator) {
		return false;
	}
}
