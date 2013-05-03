package cz.juzna.intellij.neon.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import cz.juzna.intellij.neon.psi.NeonScalar;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Complete keywords
 */
public class KeywordCompletionProvider extends CompletionProvider<CompletionParameters> {
	private static final String[] KEYWORDS = {
		// common
		"true", "false", "yes", "no", "null"
	};

	// CompletionResultSet wants list of LookupElements
	private List<LookupElementBuilder> KEYWORD_LOOKUPS = new ArrayList<LookupElementBuilder>();
	private HashMap<String, String[]> knownKeys = new HashMap<String, String[]>();
	private HashMap<String, String[]> knownValues = new HashMap<String, String[]>();


	public KeywordCompletionProvider() {
		super();
		for(String keyword: KEYWORDS) KEYWORD_LOOKUPS.add(LookupElementBuilder.create(keyword));

		knownKeys.put("", new String[] {
			// sections
			"common", "production", "development", "test", // TODO: configurable
			// extensions
			"parameters", "nette", "services", "factories", "php"
		});

		knownKeys.put("nette", new String[] {
			"session", "application", "routing", "security", "mailer", "database", "forms", "latte", "container", "debugger"
		});
		knownKeys.put("nette.session", new String[] { "debugger", "iAmUsingBadHost", "autoStart", "expiration" } );
		knownKeys.put("nette.application", new String[] { "debugger", "errorPresenter", "catchExceptions", "mapping" } );
		knownKeys.put("nette.routing", new String[] { "debugger", "routes" } );


		knownValues.put("nette.security.frames", new String[] { "DENY", "SAMEORIGIN", "ALLOW-FROM " } );
	}

	@Override
	protected void addCompletions(@NotNull CompletionParameters params,
	                              ProcessingContext ctx,
	                              @NotNull CompletionResultSet results) {

		PsiElement curr = params.getPosition().getOriginalElement();
		boolean hasSomething = false;

		if (curr.getParent() instanceof NeonKey) { // key autocompletion
			String[] parent = getKeyChain(curr.getParent().getParent().getParent()); // literal -> key -> key-val pair -> any parent
			for(String keyName : getCompletionForSection(knownKeys, parent)) {
				hasSomething = true;
				results.addElement(LookupElementBuilder.create(keyName));
			}
		}
		else if (curr.getParent() instanceof NeonScalar) { // value autocompletion
			for(LookupElementBuilder x: KEYWORD_LOOKUPS) results.addElement(x);

			String[] parent = getKeyChain(curr);

			// try suggest keys (perhaps this wannabe value will turn into a key, once assignment operator is written
			for(String keyName : getCompletionForSection(knownKeys, parent)) {
				hasSomething = true;
				results.addElement(LookupElementBuilder.create(keyName));
			}

			// smart values
			if ( ! hasSomething) for(String value : getCompletionForSection(knownValues, parent)) {
				hasSomething = true;
				results.addElement(LookupElementBuilder.create(value));
			}
		}

		if (hasSomething && params.getInvocationCount() <= 1) {
			results.stopHere();
		}
	}

	private String[] getCompletionForSection(HashMap<String, String[]> options, String[] parent) {
		List<String> ret = new ArrayList<String>();

		for (int i = 0; i < parent.length; i++) {
			String parentName = StringUtils.join(parent, ".", i, parent.length);

			String[] found = options.get(parentName);
			if (found != null) {
				Collections.addAll(ret, found);
			}
		}

		return ret.toArray(new String[ret.size()]);
	}


	/**
	 * Get full name of property at given element (e.g. common.services.myService1.setup)
	 */
	public static String[] getKeyChain(PsiElement el) {
		List<String> names = new ArrayList<String>();

		while (el != null) {
			if (el instanceof NeonKeyValPair) {
				names.add(0, ((NeonKeyValPair) el).getKeyText());
			}

			el = el.getParent();
		}
		return names.toArray(new String[names.size()]);
	}

	public static String getKeyChainString(PsiElement el) {
		return StringUtils.join(getKeyChain(el), ".");
	}

}
