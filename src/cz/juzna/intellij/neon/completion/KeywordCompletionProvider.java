package cz.juzna.intellij.neon.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import cz.juzna.intellij.neon.NeonIcons;
import cz.juzna.intellij.neon.completion.insert.KeywordInsertHandler;
import cz.juzna.intellij.neon.completion.schema.SchemaProvider;
import cz.juzna.intellij.neon.config.NeonConfiguration;
import cz.juzna.intellij.neon.config.NeonExtensionItem;
import cz.juzna.intellij.neon.config.NeonKeyChain;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Complete keywords
 */
public class KeywordCompletionProvider extends CompletionProvider<CompletionParameters> {

	private List<LookupElementBuilder> COMMON_VALUES_LOOKUPS = new ArrayList<LookupElementBuilder>();

	public KeywordCompletionProvider() {
		super();
		for (String keyword : NeonConfiguration.COMMON_VALUES) COMMON_VALUES_LOOKUPS.add(LookupElementBuilder.create(keyword));
	}

	@Override
	protected void addCompletions(
		@NotNull CompletionParameters params,
	    ProcessingContext ctx,
	    @NotNull CompletionResultSet results
	) {
		PsiElement curr = params.getPosition().getOriginalElement();
		if (curr.getNode().getElementType() == NeonTokenTypes.NEON_COMMENT) {
			return;
		}
		boolean hasSomething = false;

		PrefixMatcher prefixMatcher = results.getPrefixMatcher();
		PsiElement parentKey = curr.getParent().getParent();
		NeonKeyValPair keyValuePair = CompletionUtil.findCurrentKeyValuePair(curr);
		// hit autocompletion twice -> autodetect
//		if (params.getInvocationCount() >= 2)
		{
			SchemaProvider schemaProvider = new SchemaProvider();
			Map<String, Collection<String>> tmp = schemaProvider.getKnownTypes(curr.getProject());

			// dodgy: remap type
			HashMap<String, String[]> tmp2 = new HashMap<String, String[]>();
			for (String k : tmp.keySet()) {
				tmp2.put(k, tmp.get(k).toArray(new String[tmp.get(k).size()]));
			}

			//todo: add completion for extensions
			NeonKeyChain parent = CompletionUtil.getKeyChain(resolveKeyElementForChain(curr, false));
			for (NeonExtensionItem extension : getCompletionForKeys(tmp2, parent, keyValuePair)) {
				if (prefixMatcher.prefixMatches(extension.name)) {
					hasSomething = true;
					results.addElement(createKeyElementBuilder(extension));
				}
			}

			if (hasSomething && params.getInvocationCount() <= 1) {
				results.stopHere();
			}
		}

		boolean incompleteKey = CompletionUtil.isIncompleteKey(curr);
		if (parentKey instanceof NeonKey/* && ((NeonKey) parentKey).isMainKey())*/ || incompleteKey) { // key autocompletion
			if (attachMainKeys(curr, prefixMatcher, results, keyValuePair, incompleteKey)) {
				hasSomething = true;
			}
		}

		if (hasSomething && params.getInvocationCount() <= 1) {
			results.stopHere();
			return;
		}

		//todo: better detect if it is value, for example for: database|test|options
		if (curr.getParent() instanceof NeonScalar && !(curr.getParent().getParent() instanceof NeonKey)) { // value autocompletion
			// smart values
			if (!hasSomething) {
				NeonKeyChain chain = CompletionUtil.getKeyChain(curr);
				for (NeonExtensionItem extension : getCompletionForValues(chain, keyValuePair)) {
					if (prefixMatcher.prefixMatches(extension.name)) {
						for (String value : extension.getPossibleValues()) {
							hasSomething = true;
							results.addElement(LookupElementBuilder.create(value));
						}
					}
				}
			}

			if (hasSomething && params.getInvocationCount() <= 1) {
				results.stopHere();
				return;
			}

			for (LookupElementBuilder x : COMMON_VALUES_LOOKUPS) {
				results.addElement(x);
			}
		}

		if (hasSomething && params.getInvocationCount() <= 1) {
			results.stopHere();
		}
	}

	private boolean attachMainKeys(
		@NotNull PsiElement curr,
		@NotNull PrefixMatcher prefixMatcher,
		@NotNull CompletionResultSet results,
		@Nullable NeonKeyValPair keyValuePair,
		boolean incompleteKey
	) {
		NeonKeyChain parent;
		if (keyValuePair == null) {
			parent = CompletionUtil.getKeyChain(resolveKeyElementForChain(curr, incompleteKey));
		} else {
			parent = keyValuePair.getKey().getKeyChain(incompleteKey);
		}

		boolean hasSomething = false;
		for (NeonExtensionItem extension : getCompletionForKeys(parent, keyValuePair)) {
			if (prefixMatcher.prefixMatches(extension.name)) {
				hasSomething = true;
				LookupElementBuilder element = createKeyElementBuilder(extension);
				results.addElement(element);
			}
		}
		return hasSomething;
	}

	private LookupElementBuilder createKeyElementBuilder(@NotNull NeonExtensionItem extension) {
		return LookupElementBuilder.create(extension.name)
				.withPresentableText(extension.name)
				.withIcon(NeonIcons.KEY)
				.withTypeText(extension.getPhpType().toReadableString())
				.withInsertHandler(KeywordInsertHandler.getInstance());
	}

	private List<NeonExtensionItem> getCompletionForValues(@NotNull NeonKeyChain parentChain, @Nullable NeonKeyValPair keyValuePair) {
		return getCompletionForKeys(parentChain.getParentChain(), keyValuePair);
	}

	private List<NeonExtensionItem> getCompletionForKeys(HashMap<String, String[]> options, NeonKeyChain parentChain, @Nullable NeonKeyValPair keyValuePair) {
		//todo: use options
		return Collections.emptyList();
	}

	private List<NeonExtensionItem> getCompletionForKeys(NeonKeyChain parentChain, @Nullable NeonKeyValPair keyValuePair) {
		return new ArrayList<NeonExtensionItem>(NeonConfiguration.INSTANCE.findExtensions(parentChain, keyValuePair).values());
	}

	private static PsiElement resolveKeyElementForChain(PsiElement el, boolean isIncomplete)
	{
		if (isIncomplete) {
			return el.getParent();
		} else if (el.getParent().getParent() instanceof NeonFile) {
			return el.getParent();
		} else  {
			// literal -> scalar -> [key ->] key-val pair -> any parent
			el = el.getParent().getParent();
			return el instanceof NeonKey ? el.getParent().getParent() : el.getParent();
		}
	}

}
