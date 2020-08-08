package cz.juzna.intellij.neon.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import cz.juzna.intellij.neon.NeonIcons;
import cz.juzna.intellij.neon.completion.CompletionUtil;
import cz.juzna.intellij.neon.completion.insert.NeonParameterInsertHandler;
import cz.juzna.intellij.neon.config.NeonConfiguration;
import cz.juzna.intellij.neon.config.NeonParameter;
import cz.juzna.intellij.neon.psi.NeonKeyUsage;
import cz.juzna.intellij.neon.psi.NeonParameterUsage;
import cz.juzna.intellij.neon.psi.NeonValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Complete parameters
 */
public class ParameterCompletionProvider extends CompletionProvider<CompletionParameters> {
	// current element
	PsiElement curr;

	public ParameterCompletionProvider() {
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
		if (incompleteKey || (!(curr.getParent() instanceof NeonParameterUsage) && !(curr.getParent() instanceof NeonValue)) || curr.getParent() instanceof NeonKeyUsage) {
			return;
		}

		boolean haveSome = false;
		List<NeonParameter> definitions = NeonConfiguration.INSTANCE.findParameters(curr.getProject());
		for (NeonParameter parameter : definitions) {
			String text = parameter.getName();
			LookupElementBuilder lookupElement = LookupElementBuilder.create(text);
			lookupElement = lookupElement.withIcon(NeonIcons.PARAMETER);
			lookupElement = lookupElement.withInsertHandler(NeonParameterInsertHandler.getInstance());
			lookupElement = lookupElement.withTypeText(parameter.getPhpType().toReadableString());
			results.addElement(lookupElement);
			haveSome = true;
		}

		if (haveSome && (curr.getParent() instanceof NeonParameterUsage || curr.getText().startsWith("%"))) {
			results.stopHere();
		}
	}

}
