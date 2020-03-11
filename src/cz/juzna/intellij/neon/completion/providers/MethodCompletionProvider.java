package cz.juzna.intellij.neon.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.completion.PhpLookupElement;
import com.jetbrains.php.completion.insert.PhpMethodInsertHandler;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import cz.juzna.intellij.neon.completion.CompletionUtil;
import cz.juzna.intellij.neon.config.NeonConfiguration;
import cz.juzna.intellij.neon.config.NeonService;
import cz.juzna.intellij.neon.psi.*;
import cz.juzna.intellij.neon.psi.impl.NeonPsiImplUtil;
import cz.juzna.intellij.neon.util.NeonTypesUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Complete parameters
 */
public class MethodCompletionProvider extends CompletionProvider<CompletionParameters> {
	// current element
	PsiElement curr;

	public MethodCompletionProvider() {
		super();
	}

	@Override
	protected void addCompletions(
		@NotNull CompletionParameters params,
		ProcessingContext ctx,
	    @NotNull CompletionResultSet results
	) {
		curr = params.getPosition().getOriginalElement();
		boolean incompleteKey = CompletionUtil.isIncompleteKey(curr);
		if (incompleteKey || (!(curr.getParent() instanceof NeonMethodUsage) && !(curr.getParent() instanceof NeonValue))) {
			return;
		}

		String serviceName = NeonPsiImplUtil.getServiceName(curr);
		if (serviceName.length() == 0) {
			return;
		}

		boolean hasSomething = false;
		NeonService service = NeonConfiguration.INSTANCE.findService(serviceName, curr.getProject());
		if (service != null && service.getPhpType().containsClasses()) {
			boolean isMagicPrefixed = results.getPrefixMatcher().getPrefix().startsWith("__");
			for (PhpClass phpClass : service.getPhpType().getPhpClasses(curr.getProject())) {
				for (Method method : phpClass.getMethods()) {
					if (!method.isStatic() && phpClass.getModifier().isPublic()) {
						if (!isMagicPrefixed && params.getInvocationCount() <= 1 && NeonTypesUtil.isExcludedCompletion(method.getName())) {
							continue;
						}

						PhpLookupElement lookupItem = new PhpLookupElement(method);
						lookupItem.handler = PhpMethodInsertHandler.getInstance();
						results.addElement(lookupItem);
						hasSomething = true;
					}
				}
			}
		}
		if (hasSomething && params.getInvocationCount() <= 1) {
			results.stopHere();
		}
	}

}
