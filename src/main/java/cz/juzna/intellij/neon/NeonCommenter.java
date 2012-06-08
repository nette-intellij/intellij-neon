package cz.juzna.intellij.neon;

import com.intellij.lang.Commenter;

/**
 * Comment line/block
 */
public class NeonCommenter implements Commenter {
	@Override
	public String getLineCommentPrefix() {
		return "#";
	}

	@Override
	public String getBlockCommentPrefix() {
		return null;
	}

	@Override
	public String getBlockCommentSuffix() {
		return null;
	}

	@Override
	public String getCommentedBlockCommentPrefix() {
		return null;
	}

	@Override
	public String getCommentedBlockCommentSuffix() {
		return null;
	}
}
