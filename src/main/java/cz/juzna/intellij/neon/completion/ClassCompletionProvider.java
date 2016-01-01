package cz.juzna.intellij.neon.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.completion.PhpLookupElement;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import cz.juzna.intellij.neon.completion.insert.PhpReferenceInsertHandler;
import cz.juzna.intellij.neon.psi.NeonEntity;
import cz.juzna.intellij.neon.psi.NeonScalar;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

		PsiElement curr = params.getPosition().getOriginalElement();
		if (!(curr.getParent() instanceof NeonEntity) && !(curr.getParent() instanceof NeonScalar)) return;

		Collection<PhpNamedElement> variants = new THashSet<PhpNamedElement>();
		PhpIndex phpIndex = PhpIndex.getInstance(curr.getProject());

		PrefixMatcher prefixMatcher = results.getPrefixMatcher();
		String prefix = prefixMatcher.getPrefix();
		String namespace = null;
		if (prefix.contains("\\")) {
			int index = prefix.lastIndexOf("\\");
			namespace = prefix.substring(0, index);
			prefixMatcher = prefixMatcher.cloneWithPrefix(prefix.substring(index + 1));
		}
		Collection<String> classNames = phpIndex.getAllClassNames(prefixMatcher);

		for (String name : classNames) {
			variants.addAll(filterClasses(phpIndex.getClassesByName(name), namespace));
			variants.addAll(filterClasses(phpIndex.getInterfacesByName(name), namespace));
		}

		// Add variants
		for (PhpNamedElement item : variants) {
			PhpLookupElement lookupItem = new PhpLookupElement(item) {
				@Override
				public Set<String> getAllLookupStrings() {
					Set<String> original = super.getAllLookupStrings();
					Set<String> strings = new HashSet<String>(original.size() + 1);
					strings.addAll(original);
					strings.add(this.getNamedElement().getFQN().substring(1));
					return strings;
				}
			};
			lookupItem.handler = PhpReferenceInsertHandler.getInstance();

			results.addElement(lookupItem);
		}
	}

	private Collection<PhpClass> filterClasses(Collection<PhpClass> classes, String namespace) {
		if (namespace == null) {
			return classes;
		}
		namespace = "\\" + namespace + "\\";
		Collection<PhpClass> result = new ArrayList<PhpClass>();
		for (PhpClass cls : classes) {
			String classNs = cls.getNamespaceName();
			if (classNs.equals(namespace) || classNs.startsWith(namespace)) {
				result.add(cls);
			}
		}
		return result;
	}

}
