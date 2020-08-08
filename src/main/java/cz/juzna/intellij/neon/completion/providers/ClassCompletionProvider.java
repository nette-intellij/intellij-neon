package cz.juzna.intellij.neon.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.completion.PhpCompletionUtil;
import com.jetbrains.php.completion.PhpLookupElement;
import com.jetbrains.php.completion.insert.PhpClassStaticInsertHandler;
import com.jetbrains.php.completion.insert.PhpNamespaceInsertHandler;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import cz.juzna.intellij.neon.completion.CompletionUtil;
import cz.juzna.intellij.neon.completion.insert.PhpReferenceInsertHandler;
import cz.juzna.intellij.neon.psi.NeonClassUsage;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonNamespaceReference;
import cz.juzna.intellij.neon.psi.NeonValue;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
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
	protected void addCompletions(
			@NotNull CompletionParameters params,
			ProcessingContext ctx,
			@NotNull CompletionResultSet results
	) {
		PsiElement curr = params.getPosition().getOriginalElement();
		boolean incompleteKey = CompletionUtil.isIncompleteKey(curr);
		if (!incompleteKey && !(curr.getParent() instanceof NeonValue) && !(curr.getParent() instanceof NeonKey) && !(curr.getParent() instanceof NeonClassUsage)) {
			return;
		}

		String prefix = results.getPrefixMatcher().getPrefix();
		if (curr.getParent() instanceof NeonClassUsage) {
			if (curr instanceof NeonNamespaceReference) {
				prefix = ((NeonNamespaceReference) curr).getNamespaceName();
			} else {
				prefix = ((NeonClassUsage) curr.getParent()).getClassName();
			}
		}

		String namespace = null;
		if (prefix.contains("\\")) {
			int index = prefix.lastIndexOf("\\");
			namespace = prefix.substring(0, index) + "\\";
			prefix = prefix.substring(index + 1);
			if (!(curr.getParent() instanceof NeonClassUsage)) {
				results = results.withPrefixMatcher(prefix);
			}
		}

		Project project = params.getPosition().getProject();
		Collection<String> classNames = NeonPhpUtil.getAllExistingClassNames(project, results.getPrefixMatcher());
		Collection<PhpNamedElement> variants = NeonPhpUtil.getAllClassNamesAndInterfaces(project, classNames, namespace);

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
			lookupItem.handler = PhpReferenceInsertHandler.getInstance(incompleteKey);

			results.addElement(lookupItem);
		}
	}
/*
	@Override
	protected void addCompletions(
		@NotNull CompletionParameters params,
		ProcessingContext ctx,
		@NotNull CompletionResultSet results
	) {
		PsiElement curr = params.getPosition().getOriginalElement();
		boolean incompleteKey = CompletionUtil.isIncompleteKey(curr);
		if (!incompleteKey && !(curr.getParent() instanceof NeonValue) && !(curr.getParent() instanceof NeonKey) && !(curr.getParent() instanceof NeonClassUsage)) {
			return;
		}

		Collection<PhpNamedElement> variants = new THashSet<PhpNamedElement>();
		PhpIndex phpIndex = PhpIndex.getInstance(curr.getProject());

		PrefixMatcher prefixMatcher = results.getPrefixMatcher();
		String namespace = null;
		String prefix = prefixMatcher.getPrefix();
		if (curr.getParent() instanceof NeonClassUsage) {
			if (curr instanceof NeonNamespaceReference) {
				prefix = ((NeonNamespaceReference) curr).getNamespaceName();
			} else {
				prefix = ((NeonClassUsage) curr.getParent()).getClassName();
			}

		}

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
			lookupItem.handler = PhpReferenceInsertHandler.getInstance(incompleteKey);

			results.addElement(lookupItem);
		}
	}

	private Collection<PhpClass> filterClasses(Collection<PhpClass> classes, String namespace) {
		if (namespace == null) {
			return classes;
		}
		namespace = NeonPhpUtil.normalizeClassName(namespace);
		Collection<PhpClass> result = new ArrayList<PhpClass>();
		for (PhpClass cls : classes) {
			String classNs = cls.getNamespaceName();
			if (classNs.startsWith("\\Tester")) {
				continue;
			}
			if (classNs.equals(namespace) || classNs.startsWith(namespace)) {
				result.add(cls);
			}
		}
		return result;
	}
*/
}
