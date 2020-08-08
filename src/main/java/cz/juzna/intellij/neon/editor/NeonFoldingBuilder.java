package cz.juzna.intellij.neon.editor;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Fold sections in Neon
 */
public class NeonFoldingBuilder implements FoldingBuilder, NeonTokenTypes
{
	@NotNull
	public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode astNode, @NotNull Document document) {
		List<FoldingDescriptor> descriptors = new LinkedList<FoldingDescriptor>();
		collectDescriptors(astNode, descriptors);
		return descriptors.toArray(new FoldingDescriptor[0]);
	}

	@Nullable
	public String getPlaceholderText(@NotNull ASTNode node) {
		IElementType type = node.getElementType();
		if (NeonTokenTypes.ARRAYS.contains(type)) {
			ASTNode[] children = node.getChildren(TokenSet.create(NeonTokenTypes.KEY_VAL_PAIR));
			List<String> text = new ArrayList<String>();
			for (ASTNode child : children) {
				for (ASTNode key : child.getChildren(TokenSet.create(NeonTokenTypes.KEY))) {
					text.add(key.getText());
				}
			}
			return text.size() > 0 ? " " + String.join(" ", text.toArray(new String[0])) : "...";

		} else if (type == NeonTokenTypes.SCALAR_VALUE) {
			return node.getText().substring(0, 1);
		}
		return "...";
	}

	public boolean isCollapsedByDefault(@NotNull ASTNode node) {
		return false;
	}

	private static void collectDescriptors(@NotNull ASTNode node, @NotNull List<FoldingDescriptor> descriptors) {
		IElementType type = node.getElementType();
		TextRange nodeTextRange = node.getTextRange();
		if ((!StringUtil.isEmptyOrSpaces(node.getText())) && (nodeTextRange.getLength() >= 2)) {
			if (NeonTokenTypes.ARRAYS.contains(type)) {
				ASTNode[] children = node.getChildren(TokenSet.create(KEY_VAL_PAIR));

				if ((children.length > 0) && (!StringUtil.isEmpty(children[0].getText().trim()))) {
					descriptors.add(new FoldingDescriptor(node, nodeTextRange));
				}

			} else if (type == NeonTokenTypes.SCALAR_VALUE) {
				descriptors.add(new FoldingDescriptor(node, nodeTextRange));
			}
		}
		for (ASTNode child : node.getChildren(null)) {
			collectDescriptors(child, descriptors);
		}
	}
}
