package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;

/**
 * Better faster stronger lexer
 */
public class NeonLexer2 extends LexerBase {
	int startOffset;
	int endOffset;
	int readUntil;
	Matcher m;
	private IElementType currentType;
	private String currentContent;
	private int currentOffset;
	private CharSequence buffer;

	@Override
	public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
		this.buffer = buffer;
		m = NeonTokenizer.createPattern().matcher(buffer.subSequence(startOffset, endOffset));
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.readUntil = startOffset;

		this.advance();
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
			return currentOffset + (currentContent != null ? currentContent.length() : 0);
		}
	}

	@Override
	public void advance() {
		if (readUntil >= currentOffset) {
			if (m.find()) {
				Pair<IElementType, String> t = NeonTokenizer.getToken(m);
				currentType = t.first;
				currentContent = t.second;
				currentOffset = m.start() + startOffset;
				readUntil = currentOffset + currentContent.length();
			} else {
				currentType = null;
				currentContent = null;
				currentOffset = readUntil;
			}
		} else {
			readUntil = currentOffset;
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

}
