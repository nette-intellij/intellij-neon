package cz.juzna.intellij.neon.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.completion.PhpLookupElement;
import com.jetbrains.php.completion.insert.PhpFieldInsertHandler;
import com.jetbrains.php.completion.insert.PhpMethodInsertHandler;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import cz.juzna.intellij.neon.completion.CompletionUtil;
import cz.juzna.intellij.neon.completion.insert.PhpVariableInsertHandler;
import cz.juzna.intellij.neon.config.NeonConfiguration;
import cz.juzna.intellij.neon.config.NeonService;
import cz.juzna.intellij.neon.psi.*;
import cz.juzna.intellij.neon.psi.impl.NeonPsiImplUtil;
import cz.juzna.intellij.neon.util.NeonTypesUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Complete parameters
 */
public class PhpCompletionProvider extends CompletionProvider<CompletionParameters> {
	// current element
	PsiElement curr;

	boolean isMagicPrefixed;

	public PhpCompletionProvider() {
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

		boolean isValid = false;
		String serviceName = NeonPsiImplUtil.getServiceName(curr);
		if (curr.getParent() instanceof NeonPhpElementUsage) {
			NeonPhpStatementElement statement = ((NeonPhpElementUsage) curr.getParent()).getPhpStatement();
			if (statement != null) {
				findServiceInStatement(statement.isStatic(), (NeonPhpElementUsage) curr.getParent(), params, results);
				isValid = true;
			}

		} else if (serviceName.length() > 0) {
			findServiceByName(serviceName, params, results);
			isValid = true;
		}

		if (isValid && params.getInvocationCount() <= 1) {
			results.stopHere();
		}
	}

	private void findServiceInStatement(
			boolean isStatic,
			@NotNull NeonPhpElementUsage usageElement,
			@NotNull CompletionParameters params,
			@NotNull CompletionResultSet results
	) {
		for (PhpClass phpClass : usageElement.getPhpType().getPhpClasses(curr.getProject())) {
			for (Method method : phpClass.getMethods()) {
				if (method.getModifier().isPublic() && ((isStatic && method.getModifier().isStatic()) || (!isStatic && !method.getModifier().isStatic()))) {
					attachMethod(method, params, results);
				}
			}

			for (Field field : phpClass.getFields()) {
				if (!field.getModifier().isPublic()) {
					continue;
				}

				if (isStatic && (field.isConstant() || field.getModifier().isStatic())) {
					attachField(field, results);
				} else if (!isStatic && !field.getModifier().isStatic()) {
					attachField(field, results);
				}
			}
		}
	}

	private void findServiceByName(@NotNull String serviceName, @NotNull CompletionParameters params, @NotNull CompletionResultSet results) {
		NeonService service = NeonConfiguration.INSTANCE.findService(serviceName, curr.getProject());
		if (service != null && service.getPhpType().containsClasses()) {
			for (PhpClass phpClass : service.getPhpType().getPhpClasses(curr.getProject())) {
				for (Method method : phpClass.getMethods()) {
					if (!method.isStatic() && method.getModifier().isPublic()) {
						attachMethod(method, params, results);
					}
				}
			}
		}
	}

	private void attachMethod(@NotNull Method method, @NotNull CompletionParameters params, @NotNull CompletionResultSet results) {
		if (!isMagicPrefixed && params.getInvocationCount() <= 1 && NeonTypesUtil.isExcludedCompletion(method.getName())) {
			return;
		}

		PhpLookupElement lookupItem = new PhpLookupElement(method);
		lookupItem.handler = PhpMethodInsertHandler.getInstance();
		results.addElement(lookupItem);
	}

	private void attachField(@NotNull Field field, @NotNull CompletionResultSet results) {
		PhpLookupElement lookupItem = new PhpLookupElement(field);
		if (!field.isConstant() && field.getModifier().isStatic()) {
			lookupItem.handler = PhpVariableInsertHandler.getInstance();
		} else {
			lookupItem.handler = PhpFieldInsertHandler.getInstance();
		}
		results.addElement(lookupItem);
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
