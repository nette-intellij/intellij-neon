package cz.juzna.intellij.neon.completion.providers;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIcons;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.completion.PhpCompletionUtil;
import com.jetbrains.php.completion.PhpNamespacePrefixMatcher;
import com.jetbrains.php.completion.insert.PhpNamespaceInsertHandler;
import com.jetbrains.php.lang.PhpLangUtil;
import cz.juzna.intellij.neon.psi.NeonClassUsage;
import cz.juzna.intellij.neon.psi.NeonNamespaceReference;
import cz.juzna.intellij.neon.psi.NeonScalarValue;
import cz.juzna.intellij.neon.psi.NeonValue;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.function.Consumer;


public class NamespaceCompletionProvider extends CompletionProvider<CompletionParameters> {

	public NamespaceCompletionProvider() {
	}

	@Override
	protected void addCompletions(@NotNull CompletionParameters params, ProcessingContext context, @NotNull CompletionResultSet result) {
		PsiElement curr = params.getPosition().getOriginalElement();
		if (!(curr.getParent() instanceof NeonValue) && !(curr.getParent() instanceof NeonClassUsage)) return;
		String prefix = result.getPrefixMatcher().getPrefix();
		if (curr.getParent() instanceof NeonClassUsage) {
			if (curr instanceof NeonNamespaceReference) {
				prefix = ((NeonNamespaceReference) curr).getNamespaceName();
			} else {
				prefix = ((NeonClassUsage) curr.getParent()).getClassName();
			}
		}

		PhpIndex phpIndex = PhpIndex.getInstance(curr.getProject());
		String namespace = "";
		if (prefix.contains("\\")) {
			int index = prefix.lastIndexOf("\\");
			namespace = prefix.substring(0, index) + "\\";
			prefix = prefix.substring(index + 1);
			if (!(curr.getParent() instanceof NeonClassUsage)) {
				result = result.withPrefixMatcher(prefix);
			}
		}
		PhpCompletionUtil.addSubNamespaces(namespace, result, phpIndex, PhpNamespaceInsertHandler.getInstance());
	}

}
