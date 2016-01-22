package cz.juzna.intellij.neon.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.util.ProcessingContext;
import cz.juzna.intellij.neon.completion.schema.SchemaProvider;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.parser.NeonParser;
import cz.juzna.intellij.neon.psi.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private HashMap<Pattern, String[]> knownKeysPattern = new HashMap<Pattern, String[]>();
	private HashMap<String, String[]> knownValues = new HashMap<String, String[]>();
	private HashMap<String, String> deprecatedKeys = new HashMap<String, String>();


	public KeywordCompletionProvider() {
		super();
		for (String keyword : KEYWORDS) KEYWORD_LOOKUPS.add(LookupElementBuilder.create(keyword));

		knownKeys.put("", new String[]{
				"parameters", "nette", "services", "php", "extensions", "application", "forms",
				"http", "latte", "mail", "routing", "security", "session", "tracy", "database", "di", "inject", "decorator"
		});

		knownKeys.put("nette", new String[]{
				"session", "application", "routing", "security", "mailer", "database", "forms", "latte", "container", "debugger"
		});
		knownKeys.put("nette.application", new String[]{"debugger", "errorPresenter", "catchExceptions", "mapping"});
		knownKeys.put("nette.routing", new String[]{"debugger", "routes"});

		knownKeys.put("application", new String[]{"debugger", "errorPresenter", "catchExceptions", "mapping",
				"scanDirs", "scanComposer", "scanFilter", "silentLinks"});
		knownKeys.put("forms", new String[]{"messages"});
		knownKeys.put("http", new String[]{"proxy", "headers", "frames"});
		knownKeys.put("latte", new String[]{"xhtml", "macros"});
		knownKeys.put("mail", new String[]{"smtp", "host", "port", "username", "password", "secure", "timeout"});
		knownKeys.put("routing", new String[]{"debugger", "routes", "cache"});
		knownKeys.put("security", new String[]{"debugger", "users", "roles", "resources"});
		knownKeys.put("session", new String[]{"debugger", "autoStart", "expiration"});
		knownKeys.put("di", new String[]{"debugger", "accessors"});
		knownKeys.put("tracy", new String[]{"email", "fromEmail", "logSeverity", "editor", "browser", "errorTemplate",
				"strictMode", "maxLen", "maxDepth", "showLocation", "scream", "bar", "blueScreen"});

		String[] databaseOptions = new String[]{"dsn", "user", "password", "options", "debugger", "explain", "reflection", "conventions", "autowired"};

		knownKeys.put("database", databaseOptions);
		knownKeysPattern.put(Pattern.compile("^database\\.[\\w_-]+$"), databaseOptions);

		knownKeysPattern.put(Pattern.compile("^decorator\\.[\\w_\\\\]+$"), new String[]{"setup", "tags", "inject"});

		String[] serviceKeys = {"class", "create", "factory", "implement", "setup", "tags", "arguments", "autowired", "parameters", "inject"};
		knownKeysPattern.put(Pattern.compile("^services(\\.[\\w_-]+)?$"), serviceKeys);

		knownValues.put("http.frames", new String[]{"DENY", "SAMEORIGIN", "ALLOW-FROM "});

		deprecatedKeys.put("nette.security.frames", "http.frames");
		deprecatedKeys.put("nette.mailer", "mail");
		deprecatedKeys.put("nette.container", "di");
		deprecatedKeys.put("nette.application", "application");
		deprecatedKeys.put("nette.cache", "cache");
		deprecatedKeys.put("nette.database", "database");
		deprecatedKeys.put("nette.forms", "forms");
		deprecatedKeys.put("nette.http", "http");
		deprecatedKeys.put("nette.latte", "latte");
		deprecatedKeys.put("nette.routing", "routing");
		deprecatedKeys.put("nette.security", "security");
		deprecatedKeys.put("nette.session", "session");
		deprecatedKeys.put("nette.debugger", "tracy");
	}

	@Override
	protected void addCompletions(@NotNull CompletionParameters params,
	                              ProcessingContext ctx,
	                              @NotNull CompletionResultSet results) {

		PsiElement curr = params.getPosition().getOriginalElement();
		boolean hasSomething = false;


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

			String[] parent = getKeyChain(curr.getParent().getParent().getParent()); // literal -> key -> key-val pair -> any parent
			for (String keyName : getCompletionForSection(tmp2, parent)) {
				hasSomething = true;
				results.addElement(LookupElementBuilder.create(keyName));
			}

			if (hasSomething && params.getInvocationCount() <= 1) {
				results.stopHere();
			}
		}


		boolean incompleteKey = isIncompleteKey(curr);
		if (curr.getParent() instanceof NeonKey || incompleteKey) { // key autocompletion
			String[] parent = getKeyChain(incompleteKey ? curr.getParent() : curr.getParent().getParent().getParent()); // literal -> key -> key-val pair -> any parent
			for (String keyName : getCompletionForSection(knownKeys, knownKeysPattern, parent)) {
				hasSomething = true;
				LookupElementBuilder element = LookupElementBuilder.create(keyName + (incompleteKey ? ": " : ""))
						.withPresentableText(keyName);
				results.addElement(element);
			}
		}
		if (curr.getParent() instanceof NeonScalar) { // value autocompletion
			for (LookupElementBuilder x : KEYWORD_LOOKUPS) {
				results.addElement(x);
			}


			// smart values
			if (!hasSomething) {
				String[] parent = getKeyChain(curr);
				for (String value : getCompletionForSection(knownValues, parent)) {
					hasSomething = true;
					results.addElement(LookupElementBuilder.create(value));
				}
			}
		}

		if (hasSomething && params.getInvocationCount() <= 1) {
			results.stopHere();
		}
	}
	private String[] getCompletionForSection(HashMap<String, String[]> options, String[] parent) {
		return getCompletionForSection(options, null, parent);
	}

	private String[] getCompletionForSection(HashMap<String, String[]> options, HashMap<Pattern, String[]> optionsPattern, String[] parent) {
		List<String> ret = new ArrayList<String>();

		for (int i = 0; i <= (parent.length > 0 ? 1 : 0); i++) {
			String parentName = StringUtils.join(parent, ".", i, parent.length);
			if (deprecatedKeys.containsKey(parentName)) {
				parentName = deprecatedKeys.get(parentName);
			}

			String[] found = options.get(parentName);
			if (found == null && optionsPattern != null) {
				for (Pattern pattern : optionsPattern.keySet()) {
					if (pattern.matcher(parentName).matches()) {
						found = optionsPattern.get(pattern);
						break;
					}
				}
			}
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

	private static boolean isIncompleteKey(PsiElement el) {
		if (!NeonTokenTypes.STRING_LITERALS.contains(el.getNode().getElementType())) {
			return false;
		}
		//error element
		if (el.getParent() instanceof NeonArray
				&& el.getPrevSibling() instanceof PsiErrorElement
				&& ((PsiErrorElement) el.getPrevSibling()).getErrorDescription().equals(NeonParser.EXPECTED_ARRAY_ITEM)) {
			return true;
		}
		//first scalar in file
		if (el.getParent() instanceof NeonScalar && el.getParent().getParent() instanceof NeonFile) {
			return true;
		}

		return false;
	}

}
