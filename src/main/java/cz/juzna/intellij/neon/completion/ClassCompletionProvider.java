package cz.juzna.intellij.neon.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.completion.ClassUsageContext;
import com.jetbrains.php.completion.PhpLookupElement;
import com.jetbrains.php.completion.PhpVariantsUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.PhpDocPropertyImpl;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import cz.juzna.intellij.neon.completion.insert.PhpReferenceInsertHandler;
import cz.juzna.intellij.neon.psi.*;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Complete class names
 */
public class ClassCompletionProvider extends CompletionProvider<CompletionParameters> {

	public ClassCompletionProvider() {
		super();
	}

	@Override
	protected void addCompletions(@NotNull CompletionParameters params,
	                              ProcessingContext ctx,
	                              @NotNull CompletionResultSet results) {
		Collection<PhpNamedElement> variants = new THashSet<PhpNamedElement>();

		PsiElement curr = params.getPosition().getOriginalElement();
		if (!(curr.getParent() instanceof NeonEntity) && !(curr.getParent() instanceof NeonScalarValue)) return;

		PhpIndex phpIndex = PhpIndex.getInstance(curr.getProject());
		ClassUsageContext context = (curr instanceof ClassReference) ? ((ClassReference)curr).getUsageContext() : new ClassUsageContext(false);

		// Prepare list of possible variants
		for (String name : phpIndex.getAllClassNames(results.getPrefixMatcher())) {
			variants.addAll(phpIndex.getClassesByName(name));
		}

		// Add variants
		for (PhpNamedElement item : variants) {
			PhpLookupElement lookupItem = PhpVariantsUtil.getLookupItem(item, null);
			lookupItem.handler = PhpReferenceInsertHandler.getInstance();

			results.addElement(lookupItem);
		}
	}

}
