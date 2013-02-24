package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;

/**
 *
 */
public class NeonLexer2 extends LexerBase {
	int startOffset;
	int endOffset;
	int readUntil;
	boolean shallMatch;
	Matcher m;
	private IElementType currentType;
	private String currentContent;
	private int currentOffset;
	private CharSequence buffer;

	@Override
	public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
		m = NeonTokenizer.createPattern().matcher(this.buffer = buffer.subSequence(startOffset, endOffset));
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.readUntil = startOffset;
		shallMatch = true;
	}

	@Override
	public int getState() {
		return 0;
	}

	@Nullable
	@Override
	public IElementType getTokenType() {
		if (readUntil < currentOffset) {
			return NeonTokenTypes.NEON_UNKNOWN;
		} else {
			return currentType;
		}
	}

	@Override
	public int getTokenStart() {
		if (readUntil < currentOffset) {
			return readUntil;
		} else {
			return currentOffset;
		}
	}

	@Override
	public int getTokenEnd() {
		if (readUntil < currentOffset) {
			return currentOffset;
		} else {
			return currentOffset + currentContent.length();
		}
	}

	@Override
	public void advance() {
		if (readUntil < currentOffset) {
			Pair<IElementType, String> t = NeonTokenizer.getToken(m);
			currentType = t.first;
			currentContent = t.second;
			currentOffset = m.start() + endOffset;
		}
	}

	@Override
	public CharSequence getBufferSequence() {
		return buffer;
	}

	@Override
	public int getBufferEnd() {
		return buffer.length();
	}


	private void fetchToken() {
		if (shallMatch && m.find()) {
			Pair<IElementType, String> t = NeonTokenizer.getToken(m);
			currentType = t.first;
			currentContent = t.second;
			currentOffset = m.start() + endOffset;
			shallMatch = false;
		}
	}
}
