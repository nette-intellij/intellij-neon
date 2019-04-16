package cz.juzna.intellij.neon.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import cz.juzna.intellij.neon.completion.schema.SchemaProvider;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.parser.NeonElementTypes;
import cz.juzna.intellij.neon.psi.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
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
				"parameters", "nette", "services", "php", "extensions", "application", "forms", "constants", "search",
				"http", "latte", "mail", "routing", "security", "session", "tracy", "database", "di", "decorator"
		});

		knownKeys.put("nette", new String[]{
				"session", "application", "routing", "security", "mailer", "database", "forms", "latte", "container", "debugger"
		});

		knownKeys.put("application", new String[]{"debugger", "errorPresenter", "catchExceptions", "mapping",
				"scanDirs", "scanComposer", "scanFilter", "silentLinks"});
		knownKeys.put("forms", new String[]{"messages"});
		knownKeys.put("http", new String[]{"proxy", "headers", "frames", "csp", "cspReportOnly", "featurePolicy", "cookieSecure"});
		knownKeys.put("latte", new String[]{"xhtml", "macros", "templateClass", "strictTypes"});
		knownKeys.put("mail", new String[]{"smtp", "host", "port", "username", "password", "secure", "timeout", "context", "clientHost", "persistent"});
		knownKeys.put("routing", new String[]{"debugger", "routes", "routeClass", "cache"});
		knownKeys.put("security", new String[]{"debugger", "users", "roles", "resources"});
		knownKeys.put("session", new String[]{"debugger", "autoStart", "expiration", "handler",
			"cookieSamesite", "cookieDomain", "cookieLifetime", "cookiePath", "cookieSecure", "lazyWrite", "name", "savePath"});
		knownKeys.put("di", new String[]{"debugger", "accessors", "excluded", "parentClass", "export"});
		knownKeys.put("tracy", new String[]{"email", "fromEmail", "logSeverity", "editor", "browser", "errorTemplate",
				"strictMode", "maxLength", "maxDepth", "showLocation", "scream", "bar", "blueScreen", "showBar", "editorMapping", "netteMailer"});

		String[] databaseOptions = new String[]{"dsn", "user", "password", "options", "debugger", "explain", "reflection", "conventions", "autowired"};

		knownKeys.put("database", databaseOptions);
		knownKeysPattern.put(Pattern.compile("^database\\|[\\w_-]+$"), databaseOptions);

		String[] searchOptions = new String[]{"in", "files", "classes", "extends", "implements", "exclude", "tags"};

		knownKeys.put("search", searchOptions);
		knownKeysPattern.put(Pattern.compile("^search\\|[\\w_-]+$"), searchOptions);
		knownKeysPattern.put(Pattern.compile("^search\\|exclude$"), new String[]{"classes", "extends", "implements"});
		knownKeysPattern.put(Pattern.compile("^search\\|[\\w_-]+\\|exclude$"), new String[]{"classes", "extends", "implements"});

		knownKeysPattern.put(Pattern.compile("^decorator\\|[\\w_\\\\]+$"), new String[]{"setup", "tags", "inject"});

		knownKeysPattern.put(Pattern.compile("^di\\|export$"), new String[]{"parameters", "tags", "types"});

		String[] serviceKeys = {"class", "create", "factory", "implement", "setup", "tags", "arguments", "autowired", "parameters", "inject",
			"imported", "alteration", "references", "tagged", "type"};

		knownKeysPattern.put(Pattern.compile("^services\\|([\\\\.\\w_-]+|#)$"), serviceKeys);

		knownValues.put("http|frames", new String[]{"DENY", "SAMEORIGIN", "ALLOW-FROM "});

		String[] cspKeys = {"base-uri", "block-all-mixed-content", "child-src", "connect-src", "default-src", "font-src", "form-action", "frame-ancestors",
			"frame-src", "img-src", "manifest-src", "media-src", "object-src", "plugin-types", "require-sri-for", "sandbox", "script-src", "style-src",
			"upgrade-insecure-requests", "worker-src", "report-to"};

		knownKeysPattern.put(Pattern.compile("^http\\|csp$"), cspKeys);
		knownKeysPattern.put(Pattern.compile("^http\\|cspReportOnly$"), cspKeys);

		knownKeysPattern.put(Pattern.compile("^http\\|featurePolicy$"), new String[]{"autoplay", "camera", "encrypted-media", "fullscreen", "geolocation", "microphone", "midi", "payment", "vr"});

		deprecatedKeys.put("di|accessors", "");
		deprecatedKeys.put("nette|security|frames", "http|frames");
		deprecatedKeys.put("nette|mailer", "mail");
		deprecatedKeys.put("nette|container", "di");
		deprecatedKeys.put("nette|application", "application");
		deprecatedKeys.put("nette|cache", "cache");
		deprecatedKeys.put("nette|database", "database");
		deprecatedKeys.put("nette|forms", "forms");
		deprecatedKeys.put("nette|http", "http");
		deprecatedKeys.put("nette|latte", "latte");
		deprecatedKeys.put("nette|routing", "routing");
		deprecatedKeys.put("nette|security", "security");
		deprecatedKeys.put("nette|session", "session");
		deprecatedKeys.put("nette|debugger", "tracy");
	}

	@Override
	protected void addCompletions(@NotNull CompletionParameters params,
	                              ProcessingContext ctx,
	                              @NotNull CompletionResultSet results) {

		PsiElement curr = params.getPosition().getOriginalElement();
		if (curr.getNode().getElementType() == NeonTokenTypes.NEON_COMMENT) {
			return;
		}
		boolean hasSomething = false;


		PrefixMatcher prefixMatcher = results.getPrefixMatcher();
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

			String[] parent = getKeyChain(resolveKeyElementForChain(curr, false));
			for (String keyName : getCompletionForSection(tmp2, parent)) {
				if (prefixMatcher.prefixMatches(keyName)) {
					hasSomething = true;
					results.addElement(LookupElementBuilder.create(keyName));
				}
			}

			if (hasSomething && params.getInvocationCount() <= 1) {
				results.stopHere();
			}
		}


		boolean incompleteKey = CompletionUtil.isIncompleteKey(curr);
		if (curr.getParent().getParent() instanceof NeonKey || incompleteKey) { // key autocompletion
			String[] parent = getKeyChain(resolveKeyElementForChain(curr, incompleteKey));
			for (String keyName : getCompletionForSection(knownKeys, knownKeysPattern, parent)) {
				if (prefixMatcher.prefixMatches(keyName)) {
					hasSomething = true;
					LookupElementBuilder element = LookupElementBuilder.create(keyName + (incompleteKey ? ": " : ""))
							.withPresentableText(keyName);
					results.addElement(element);
				}
			}
		}
		if (curr.getParent() instanceof NeonScalar && !(curr.getParent().getParent() instanceof NeonKey)) { // value autocompletion
			for (LookupElementBuilder x : KEYWORD_LOOKUPS) {
				results.addElement(x);
			}


			// smart values
			if (!hasSomething) {
				String[] parent = getKeyChain(curr);
				for (String value : getCompletionForSection(knownValues, parent)) {
					if (prefixMatcher.prefixMatches(value)) {
						hasSomething = true;
						results.addElement(LookupElementBuilder.create(value));
					}
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
			String parentName = StringUtils.join(parent, "|", i, parent.length);
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

	/**
	 * Get full name of property at given element (e.g. common.services.myService1.setup)
	 */
	public static String[] getKeyChain(PsiElement el) {
		List<String> names = new ArrayList<String>();

		while (el != null) {
			if (el instanceof NeonKeyValPair) {
				names.add(0, ((NeonKeyValPair) el).getKeyText());
			} else if (el.getNode().getElementType() == NeonElementTypes.ITEM) {
				names.add(0, "#");
			}

			el = el.getParent();
		}
		return names.toArray(new String[names.size()]);
	}


}
