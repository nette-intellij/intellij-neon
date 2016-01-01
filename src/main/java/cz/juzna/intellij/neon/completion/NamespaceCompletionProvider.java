package cz.juzna.intellij.neon.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.completion.PhpCompletionUtil;
import com.jetbrains.php.completion.insert.PhpNamespaceInsertHandler;
import cz.juzna.intellij.neon.psi.NeonEntity;
import cz.juzna.intellij.neon.psi.NeonScalar;
import org.jetbrains.annotations.NotNull;


public class NamespaceCompletionProvider extends CompletionProvider<CompletionParameters> {

	public NamespaceCompletionProvider() {
	}

	@Override
	protected void addCompletions(@NotNull CompletionParameters params, ProcessingContext context, @NotNull CompletionResultSet result) {
		PsiElement curr = params.getPosition().getOriginalElement();
		if (!(curr.getParent() instanceof NeonEntity) && !(curr.getParent() instanceof NeonScalar)) return;
		PhpIndex phpIndex = PhpIndex.getInstance(curr.getProject());
		String prefix = result.getPrefixMatcher().getPrefix();
		String namespace = "";
		if (prefix.contains("\\")) {
			int index = prefix.lastIndexOf("\\");
			namespace = prefix.substring(0, index) + "\\";
			prefix = prefix.substring(index + 1);
		}
		PhpCompletionUtil.addSubNamespaces(namespace, result.withPrefixMatcher(prefix), phpIndex, PhpNamespaceInsertHandler.getInstance());
	}
}
