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

	boolean isMagicPrefixed;

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
		if (!isValidTarget()) {
			return;
		}

		isMagicPrefixed = results.getPrefixMatcher().getPrefix().startsWith("__");

		boolean hasSomething = false;
		String serviceName = NeonPsiImplUtil.getServiceName(curr);
		if (serviceName.length() > 0) {
			hasSomething = findServiceByName(serviceName, params, results);

		} else if (curr.getParent() instanceof NeonPhpElementUsage) {
			NeonPhpStatementElement statement = ((NeonPhpElementUsage) curr.getParent()).getPhpStatement();
			if (statement != null) {
				hasSomething = findServiceInStatement((NeonPhpElementUsage) curr.getParent(), params, results);
			}

		}

		if (hasSomething && params.getInvocationCount() <= 1) {
			results.stopHere();
		}
	}

	private boolean findServiceInStatement(
			@NotNull NeonPhpElementUsage usageElement,
			@NotNull CompletionParameters params,
			@NotNull CompletionResultSet results
	) {
		boolean hasSomething = false;
		for (PhpClass phpClass : usageElement.getPhpType().getPhpClasses(curr.getProject())) {
			for (Method method : phpClass.getMethods()) {
				if (method.getModifier().isPublic()) {
					if (attachMethod(method, params, results)) {
						hasSomething = true;
					}
				}
			}
		}
		return hasSomething;
	}

	private boolean findServiceByName(@NotNull String serviceName, @NotNull CompletionParameters params, @NotNull CompletionResultSet results) {
		boolean hasSomething = false;
		NeonService service = NeonConfiguration.INSTANCE.findService(serviceName, curr.getProject());
		if (service != null && service.getPhpType().containsClasses()) {
			for (PhpClass phpClass : service.getPhpType().getPhpClasses(curr.getProject())) {
				for (Method method : phpClass.getMethods()) {
					if (!method.isStatic() && method.getModifier().isPublic()) {
						if (attachMethod(method, params, results)) {
							hasSomething = true;
						}
					}
				}
			}
		}
		return hasSomething;
	}

	private boolean attachMethod(@NotNull Method method, @NotNull CompletionParameters params, @NotNull CompletionResultSet results) {
		if (!isMagicPrefixed && params.getInvocationCount() <= 1 && NeonTypesUtil.isExcludedCompletion(method.getName())) {
			return false;
		}

		PhpLookupElement lookupItem = new PhpLookupElement(method);
		lookupItem.handler = PhpMethodInsertHandler.getInstance();
		results.addElement(lookupItem);
		return true;
	}

	private boolean isValidTarget() {
		boolean incompleteKey = CompletionUtil.isIncompleteKey(curr);
		if (incompleteKey) {
			return false;
		}

		return curr.getParent() instanceof NeonMethodUsage
				|| curr.getParent() instanceof NeonValue
				|| curr.getParent() instanceof NeonConstantUsage;
	}

}
