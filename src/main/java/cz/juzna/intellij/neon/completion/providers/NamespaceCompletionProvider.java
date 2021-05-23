package cz.juzna.intellij.neon.completion.providers;

import com.intellij.codeInsight.completion.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.completion.PhpLookupElement;
import com.jetbrains.php.completion.insert.PhpNamespaceInsertHandler;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import cz.juzna.intellij.neon.psi.NeonClassUsage;
import cz.juzna.intellij.neon.psi.NeonValue;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NamespaceCompletionProvider extends CompletionProvider<CompletionParameters> {

	public NamespaceCompletionProvider() {
	}

	@Override
	protected void addCompletions(
		@NotNull CompletionParameters params,
		@NotNull ProcessingContext context,
		@NotNull CompletionResultSet result
	) {
		PsiElement curr = params.getPosition().getOriginalElement();
		if (!(curr.getParent() instanceof NeonValue) && !(curr.getParent() instanceof NeonClassUsage)) {
			return;
		}

		String namespaceName = getNamespaceName(curr);
		Collection<PhpNamespace> namespaceNames = NeonPhpUtil.getNamespacesByName(curr.getProject(), namespaceName);
		for (PhpNamespace namespace : namespaceNames) {
			PhpLookupElement lookupItem = getPhpLookupElement(namespace, getTypeText(namespace.getParentNamespaceName()));
			lookupItem.handler = PhpNamespaceInsertHandler.getInstance();
			result.addElement(lookupItem);
		}
	}

	@Nullable
	private String getTypeText(String parentNamespace) {
		if (parentNamespace.length() > 1) {
			if (parentNamespace.startsWith("\\")) {
				parentNamespace = parentNamespace.substring(1);
			}
			if (parentNamespace.endsWith("\\") && parentNamespace.length() > 1) {
				parentNamespace = parentNamespace.substring(0, parentNamespace.length() - 1);
			}
			return " [" + parentNamespace + "]";
		}
		return null;
	}

	private PhpLookupElement getPhpLookupElement(@NotNull PhpNamedElement phpNamedElement, @Nullable String tailText) {
		PhpLookupElement element = new PhpLookupElement(phpNamedElement) {
			@Override
			public Set<String> getAllLookupStrings() {
				Set<String> original = super.getAllLookupStrings();
				Set<String> strings = new HashSet<>(original.size() + 1);
				strings.addAll(original);
				return strings;
			}
		};
		if (tailText != null) {
			element.tailText = tailText;
		}
		return element;
	}

	private String getNamespaceName(PsiElement element) {
		NeonClassUsage classUsage = PsiTreeUtil.getParentOfType(element, NeonClassUsage.class);
		String namespaceName = "";
		if (classUsage != null) {
			String className = classUsage.getClassName().substring(1);
			if (className.length() == 1) {
				return "";
			}
			className = classUsage.getClassName();
			int index = className.lastIndexOf("\\");
			namespaceName = className.substring(0, index);
		}
		return NeonPhpUtil.normalizeClassName(namespaceName);
	}

}
