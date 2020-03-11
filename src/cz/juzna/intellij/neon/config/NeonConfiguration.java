package cz.juzna.intellij.neon.config;

import com.intellij.openapi.project.Project;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import cz.juzna.intellij.neon.util.NeonPhpType;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import cz.juzna.intellij.neon.util.NeonTypesUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

public class NeonConfiguration {
	public static final NeonConfiguration INSTANCE = new NeonConfiguration();

	public static final String KEY_CLASS = "class";
	public static final String KEY_SETUP = "setup";

	public static final String[] COMMON_VALUES = {"true", "false", "yes", "no", "null"};

	private Map<String, NeonService> standardServices = new HashMap<>();

	private Map<String, NeonParameter> standardParameters = new HashMap<>();

	private Map<String, Map<String, NeonExtensionItem>> standardExtensions = new HashMap<>();

	private Map<String, Map<String, NeonExtensionItem>> patternsOnlyExtensions = new HashMap<>();

	private Map<String, NeonExtensionItem> standardExtensionAliases = new HashMap<>();

	private Map<Pattern, NeonExtensionItem> standardExtensionPatterns = new HashMap<>();

	public NeonConfiguration() {
		initStandardParameters();
		initStandardServices();
		initStandardExtensions();
	}

	private void initStandardParameters() {
		addParameter("tempDir", "string");
		addParameter("appDir", "string");
		addParameter("wwwDir", "string");
		addParameter("vendorDir", "string");
		addParameter("debugMode", "bool");
		addParameter("productionMode", "bool");
		addParameter("consoleMode", "bool");
	}

	private void initStandardServices() {
		addService("application.application", "Nette\\Application\\Application", new String[]{"application"});
		addService("cache.storage", "Nette\\Caching\\Storages\\FileStorage", new String[]{"cacheStorage"});
		addService("database.default.connection", "Nette\\Database\\Connection", new String[]{"database.default"});
		addService("http.request", "Nette\\Http\\Request", new String[]{"httpRequest"});
		addService("http.response", "Nette\\Http\\Response", new String[]{"httpResponse"});
		addService("cache.journal", "Nette\\Caching\\Storages\\IJournal");
		addService("database.default.context", "Nette\\Database\\Context");
		addService("http.requestFactory", "Nette\\Http\\RequestFactory");
		addService("latte.latteFactory", "Latte\\Engine", new String[]{"nette.latteFactory"});
		addService("mail.mailer", "Nette\\Mail\\IMailer");
		addService("application.presenterFactory", "Nette\\Application\\PresenterFactory");
		addService("latte.templateFactory", "Nette\\Bridges\\ApplicationLatte\\TemplateFactory");
		addService("security.userStorage", "Nette\\Http\\UserStorage");
		addService("routing.router", "Nette\\Application\\Routers\\RouteList");
		addService("session.session", "Nette\\Http\\Session", new String[]{"session"});
		addService("security.user", "Nette\\Security\\User", new String[]{"user"});
		addService("security.passwords", "Nette\\Security\\Passwords");
	}

	private void initStandardExtensions() {
		addExtensionItem("parameters", type("array"));
		addExtensionItem("services", type("array"));
		addExtensionItem("extensions", type("array"));
		addExtensionItem("constants", type("array"));

		NeonExtensionItem php = addExtensionItem("php", type("array"));

		for (Map.Entry<String, NeonPhpType> entry : NeonTypesUtil.getPhpDirectives().entrySet()) {
			addExtensionItem(php.addChild(entry.getKey(), entry.getValue()));
		}

		// session
		NeonExtensionItem session = addExtensionItem("session", type("array"));
		addExtensionItem(session.addChild("debugger", type("bool")));
		NeonExtensionItem sessionAutoStart = session.addChild("autoStart", type("bool|string"));
		sessionAutoStart.addPossibleValues(new String[]{"true", "false", "smart"});
		addExtensionItem(sessionAutoStart);
		addExtensionItem(session.addChild("expiration", type("string")));
		addExtensionItem(session.addChild("cookieDomain", type("string")));
		addExtensionItem(session.addChild("cookiePath", type("string")));
		addExtensionItem(session.addChild("name", type("string")));
		addExtensionItem(session.addChild("savePath", type("string")));
		addExtensionItem(session.addChild("handler", type("string")));
		addExtensionItem(session.addChild("cookieSamesite"));
		addExtensionItem(session.addChild("cookieLifetime"));
		addExtensionItem(session.addChild("cookieSecure"));
		addExtensionItem(session.addChild("lazyWrite"));
		addExtensionItem(session.addChild("readAndClose", type("bool")));

		for (Map.Entry<String, NeonPhpType> entry : NeonTypesUtil.getPhpSessionDirectives().entrySet()) {
			addExtensionItem(session.addChild(entry.getKey(), entry.getValue()));
		}

		// mail
		NeonExtensionItem mail = addExtensionItem("mail", type("array"));
		addExtensionItem(mail.addChild("smtp", type("bool")));
		addExtensionItem(mail.addChild("host", type("string")));
		addExtensionItem(mail.addChild("port", type("int")));
		addExtensionItem(mail.addChild("username", type("string")));
		addExtensionItem(mail.addChild("password", type("string")));
		NeonExtensionItem mailSecure = addExtensionItem(mail.addChild("secure", type("string|null")));
		mailSecure.addPossibleValues(new String[]{"ssl", "tls"});
		addExtensionItem(mail.addChild("timeout", type("int")));
		addExtensionItem(mail.addChild("context", type("array[]")));
		addExtensionItem(mail.addChild("clientHost", type("string")));
		addExtensionItem(mail.addChild("persistent", type("bool")));

		NeonExtensionItem mailDkim = mail.addChild("dkim", type("array|null"));
		addExtensionItem(mailDkim);
		addExtensionItem(mailDkim.addChild("domain", type("string")));
		addExtensionItem(mailDkim.addChild("selector", type("string")));
		addExtensionItem(mailDkim.addChild("privateKey", type("string")));
		addExtensionItem(mailDkim.addChild("passPhrase", type("string")));
		addExtensionItem(mailDkim.addChild("testMode", type("bool")));

		// latte
		NeonExtensionItem latte = addExtensionItem("latte", type("array"));
		addExtensionItem(latte.addChild("xhtml", type("bool")));
		addExtensionItem(latte.addChild("macros", type("string[]")));
		addExtensionItem(latte.addChild("templateClass", type("string|null")));
		addExtensionItem(latte.addChild("strictTypes", type("bool")));

		// routing
		NeonExtensionItem routing = addExtensionItem("routing", type("array"));
		addExtensionItem(routing.addChild("debugger", type("bool")));
		addExtensionItem(routing.addChild("routes", type("string[]")));
		addExtensionItem(routing.addChild("routeClass", type("string|null")));
		addExtensionItem(routing.addChild("cache", type("bool")));

		// security
		NeonExtensionItem security = addExtensionItem("security", type("array"));
		addExtensionItem(security.addChild("debugger", type("bool")));
		addExtensionItem(security.addChild("roles", type("string[]|array[]|null[]")));
		addExtensionItem(security.addChild("resources", type("string[]|null[]")));

		NeonExtensionItem securityUsers = addExtensionItem(security.addChild("users", type("string|array")));
		addExtensionItem(securityUsers);
		addExtensionItem(securityUsers.addChild("password", type("string")));
		addExtensionItem(securityUsers.addChild("roles", type("string|string[]")));
		addExtensionItem(securityUsers.addChild("data", type("array")));

		// http
		NeonExtensionItem http = addExtensionItem("http", type("array"));
		addExtensionItem(http.addChild("proxy", type("string")));
		addExtensionItem(http.addChild("headers", type("string[]|null")));
		NeonExtensionItem httpFrames = http.addChild("frames", type("string|bool|null"));
		httpFrames.addPossibleValues(new String[]{"DENY", "SAMEORIGIN", "ALLOW-FROM "});
		addExtensionItem(httpFrames);
		addExtensionItem(http.addChild("cookieSecure", type("string|bool|null")));

		NeonExtensionItem httpFeaturePolicy = http.addChild("featurePolicy", type("array|string|null"));
		addExtensionItem(httpFeaturePolicy);
		addExtensionItem(httpFeaturePolicy.addChild("autoplay"));
		addExtensionItem(httpFeaturePolicy.addChild("camera"));
		addExtensionItem(httpFeaturePolicy.addChild("encrypted-media"));
		addExtensionItem(httpFeaturePolicy.addChild("fullscreen"));
		addExtensionItem(httpFeaturePolicy.addChild("geolocation"));
		addExtensionItem(httpFeaturePolicy.addChild("microphone"));
		addExtensionItem(httpFeaturePolicy.addChild("midi"));
		addExtensionItem(httpFeaturePolicy.addChild("payment"));
		addExtensionItem(httpFeaturePolicy.addChild("vr"));

		NeonExtensionItem httpCsp = http.addChild("csp", type("array|string|null"));
		addExtensionItem(httpCsp);
		NeonExtensionItem httpCspReportOnly = http.addChild("cspReportOnly", type("array|string|null"));
		addExtensionItem(httpCspReportOnly);

		String[] cspKeys = {
			"base-uri", "block-all-mixed-content", "child-src", "connect-src", "default-src", "font-src", "form-action",
			"frame-ancestors", "frame-src", "img-src", "manifest-src", "media-src", "object-src", "plugin-types",
			"require-sri-for", "sandbox", "script-src", "style-src", "upgrade-insecure-requests", "worker-src", "report-to"
		};
		for (String cspKey : cspKeys) {
			addExtensionItem(httpCsp.addChild(cspKey, type("string")));
			addExtensionItem(httpCspReportOnly.addChild(cspKey, type("string")));
		}

		// application
		NeonExtensionItem application = addExtensionItem("application", type("array"));
		addExtensionItem(application.addChild("debugger", type("bool")));
		addExtensionItem(application.addChild("errorPresenter", type("string")));
		addExtensionItem(application.addChild("catchExceptions", type("bool")));
		addExtensionItem(application.addChild("mapping", type("string[]|array[]")));
		addExtensionItem(application.addChild("scanDirs", type("string[]|bool")));
		addExtensionItem(application.addChild("scanComposer", type("bool")));
		addExtensionItem(application.addChild("scanFilter", type("string")));
		addExtensionItem(application.addChild("silentLinks", type("bool")));

		// forms
		NeonExtensionItem forms = addExtensionItem("forms", type("array"));
		addExtensionItem(forms.addChild("messages", type("string[]")));

		// di
		NeonExtensionItem di = addExtensionItem("di", type("array"));
		addExtensionItem(di.addChild("debugger", type("bool")));
		addExtensionItem(di.addChild("excluded", type("string[]")));
		addExtensionItem(di.addChild("parentClass", type("string|null")));
		addExtensionItem(di.addChild("accessors")); //todo: deprecated?

		NeonExtensionItem diExport = di.addChild("export", type("array"));
		addExtensionItem(diExport);
		addExtensionItem(diExport.addChild("parameters", type("bool")));
		addExtensionItem(diExport.addChild("tags", type("string[]|bool|null")));
		addExtensionItem(diExport.addChild("types", type("string[]|bool|null")));

		// tracy
		NeonExtensionItem tracy = addExtensionItem("tracy", type("array"));
		addExtensionItem(tracy.addChild("email", type("string[]|string")));
		addExtensionItem(tracy.addChild("fromEmail", type("string")));
		addExtensionItem(tracy.addChild("logSeverity", type("string|int")));
		addExtensionItem(tracy.addChild("editor", type("string")));
		addExtensionItem(tracy.addChild("browser", type("string")));
		addExtensionItem(tracy.addChild("errorTemplate", type("string")));
		addExtensionItem(tracy.addChild("strictMode", type("bool")));
		addExtensionItem(tracy.addChild("maxLength", type("int")));
		addExtensionItem(tracy.addChild("maxDepth", type("int")));
		addExtensionItem(tracy.addChild("showLocation", type("bool")));
		addExtensionItem(tracy.addChild("scream", type("bool")));
		addExtensionItem(tracy.addChild("bar", type("string[]")));
		addExtensionItem(tracy.addChild("blueScreen", type("callable[]")));
		addExtensionItem(tracy.addChild("showBar", type("bool")));
		addExtensionItem(tracy.addChild("editorMapping", type("string[]")));
		addExtensionItem(tracy.addChild("netteMailer", type("bool")));

		// database
		NeonExtensionItem database = addExtensionItem("database", type("array"));
		addExtensionPattern(Pattern.compile("^database\\|[\\w_-]+$"), database);

		addExtensionItem(database.addChild("dsn", type("string")));
		addExtensionItem(database.addChild("user", type("string")));
		addExtensionItem(database.addChild("password", type("string")));
		addExtensionItem(database.addChild("debugger", type("bool")));
		addExtensionItem(database.addChild("explain", type("bool")));
		addExtensionItem(database.addChild("reflection", type("string")));
		NeonExtensionItem databaseConventions = addExtensionItem(database.addChild("conventions", type("string")));
		databaseConventions.addPossibleValues(new String[]{"discovered"});
		addExtensionItem(database.addChild("autowired", type("bool")));

		NeonExtensionItem databaseOptions = database.addChild("options", type("array"));
		addExtensionPattern(Pattern.compile("^database\\|[\\w_-]+\\|options$"), databaseOptions);
		addExtensionItem(databaseOptions);
		addExtensionItem(databaseOptions.addChild("lazy", type("bool")));
		addExtensionItem(databaseOptions.addChild("driverClass", type("string")));

		// search
		NeonExtensionItem search = addExtensionItem("search", type("array"));
		addExtensionPattern(Pattern.compile("^search\\|[\\w_-]+$"), database);

		addExtensionItem(search.addChild("in", type("string")));
		addExtensionItem(search.addChild("files", type("string[]")));
		addExtensionItem(search.addChild("classes", type("string[]")));
		addExtensionItem(search.addChild("extends", type("string")));
		addExtensionItem(search.addChild("implements", type("string[]")));
		addExtensionItem(search.addChild("tags", type("string[]")));

		NeonExtensionItem searchExclude = search.addChild("exclude", type("array"));
		addExtensionPattern(Pattern.compile("^search\\|[\\w_-]+\\|exclude$"), databaseOptions);
		addExtensionItem(searchExclude);
		addExtensionItem(searchExclude.addChild("classes", type("string[]")));
		addExtensionItem(searchExclude.addChild("extends", type("string[]")));
		addExtensionItem(searchExclude.addChild("implements", type("string[]")));

		// decorator
		NeonExtensionItem decorator = addExtensionItem("decorator", true, true);
		decorator.setPhpType(type("array"));
		addExtensionPattern(Pattern.compile("^decorator\\|[\\w_\\\\]+$"), decorator);

		NeonExtensionItem decoratorSetup = addExtensionItem(decorator.addChild("setup", type("array")), true);
		decoratorSetup.addTag(NeonExtensionItem.Tag.SETUP_METHODS);
		addExtensionItem(decorator.addChild("tags", type("string[]")), true);
		addExtensionItem(decorator.addChild("inject", type("bool")), true);

		// services
		NeonExtensionItem services = addExtensionItem("services", true, true);
		services.setPhpType(type("array"));
		addExtensionPattern(Pattern.compile("^services\\|([\\\\.\\w_-]+|#)$"), services);

		addExtensionItem(services.addChild("class", type("string")), true);
		addExtensionItem(services.addChild("factory", type("string")), true);
		addExtensionItem(services.addChild("implement", type("string")), true);
		NeonExtensionItem servicesSetup = addExtensionItem(services.addChild("setup", type("array")), true);
		servicesSetup.addTag(NeonExtensionItem.Tag.SETUP_METHODS);
		addExtensionItem(services.addChild("tags", type("string[]")), true);
		addExtensionItem(services.addChild("autowired", type("bool")), true);
		addExtensionItem(services.addChild("inject", type("bool")), true);
		addExtensionItem(services.addChild("imported", type("bool")), true);
		addExtensionItem(services.addChild("tagged", type("bool")), true);
		addExtensionItem(services.addChild("create"), true);
		addExtensionItem(services.addChild("arguments"), true);
		addExtensionItem(services.addChild("parameters"), true);
		addExtensionItem(services.addChild("alteration"), true);
		addExtensionItem(services.addChild("references"), true);
		addExtensionItem(services.addChild("type"), true);

		// nette
		NeonExtensionItem nette = addExtensionItem("nette", NeonExtensionItem.Version.V_2_3);
		addExtensionItemAlias(nette.addChild("session", NeonExtensionItem.Version.V_2_3), session);
		addExtensionItemAlias(nette.addChild("application", NeonExtensionItem.Version.V_2_3), application);
		addExtensionItemAlias(nette.addChild("routing", NeonExtensionItem.Version.V_2_3), routing);
		addExtensionItemAlias(nette.addChild("security", NeonExtensionItem.Version.V_2_3), security);
		addExtensionItemAlias(nette.addChild("mailer", NeonExtensionItem.Version.V_2_3), mail);
		addExtensionItemAlias(nette.addChild("database", NeonExtensionItem.Version.V_2_3), database);
		addExtensionItemAlias(nette.addChild("forms", NeonExtensionItem.Version.V_2_3), forms);
		addExtensionItemAlias(nette.addChild("latte", NeonExtensionItem.Version.V_2_3), latte);
		addExtensionItemAlias(nette.addChild("container", NeonExtensionItem.Version.V_2_3), di);
		addExtensionItemAlias(nette.addChild("debugger", NeonExtensionItem.Version.V_2_3), tracy);
	}

	@NotNull
	public Map<String, Map<String, NeonExtensionItem>> getStandardExtensions() {
		return Collections.unmodifiableMap(standardExtensions);
	}

	@Nullable
	public NeonParameter findParameter(String parameterName, @NotNull Project project) {
		NeonParameter out = standardParameters.get(parameterName);
		if (out != null) {
			return out;
		}

		NeonKey key = NeonPhpUtil.findNeonParameterDefinition(parameterName, project);
		if (key != null) {
			return new NeonParameter(key.getKeyChain(false).withoutParentKey().withChildKey(key.getKeyText()).toDottedString());
		}
		return null;
	}

	public List<NeonParameter> findParameters(@NotNull Project project) {
		List<NeonParameter> out = new ArrayList<NeonParameter>(standardParameters.values());
		for (NeonKey key : NeonPhpUtil.findNeonParameterDefinitions(project)) {
			out.add(new NeonParameter(key.getKeyChain(false).withoutParentKey().withChildKey(key.getKeyText()).toDottedString()));
		}
		return out;
	}

	public List<NeonService> findServices(@NotNull Project project) {
		List<NeonService> out = new ArrayList<NeonService>(standardServices.values());
		out.addAll(NeonPhpUtil.findNeonServiceDefinitions(project));
		return out;
	}

	public NeonService findService(String serviceName, @NotNull Project project) {
		NeonService out = standardServices.get(serviceName);
		if (out != null) {
			return out;
		}

		out = NeonPhpUtil.findNeonServiceDefinition(serviceName, project);
		if (out != null) {
			return out;
		}
		return null;
	}

	@NotNull
	public Map<String, NeonExtensionItem> findExtensions(@NotNull NeonKeyChain chain, @Nullable NeonKeyValPair keyValuePair) {
		Map<String, NeonExtensionItem> out = standardExtensions.get(chain.toString());
		if (out != null) {
			return Collections.unmodifiableMap(out);
		}

		NeonExtensionItem found = null;
		for (Pattern pattern : standardExtensionPatterns.keySet()) {
			if (pattern.matcher(chain.toString()).matches()) {
				found = standardExtensionPatterns.get(pattern);
				break;
			}
		}

		if (found == null) {
			for (String alias : standardExtensionAliases.keySet()) {
				if (alias.equals(chain.toString())) {
					found = standardExtensionAliases.get(chain.toString());
					break;
				}
			}
		}

		if (found == null) {
			return Collections.emptyMap();

		} else if (found.isPatternOnly()) {
			Map<String, NeonExtensionItem> patternOnly = patternsOnlyExtensions.get(found.chain.withChildKey(found.name).toString());
			return patternOnly != null ? Collections.unmodifiableMap(patternOnly) : Collections.emptyMap();
		}

		if (keyValuePair == null) {
			return Collections.emptyMap();
		}
		return findExtensions(found.chain.withChildKey(keyValuePair.getKeyText()), null);
	}

	private void addParameter(@NotNull String name) {
		addParameter(name, "mixed");
	}

	private void addParameter(@NotNull String name, @NotNull String phpType) {
		standardParameters.put(name, new NeonParameter(name, NeonPhpType.create(phpType)));
	}

	private void addService(@NotNull String name) {
		addService(name, "mixed");
	}

	private void addService(@NotNull String name, @NotNull String phpType) {
		standardServices.put(name, new NeonService(name, NeonPhpType.create(phpType)));
	}

	private void addService(@NotNull String name, @NotNull String phpType, @NotNull String[] aliases) {
		NeonService service = new NeonService(name, NeonPhpType.create(phpType), aliases);
		standardServices.put(name, service);
		for (String alias : aliases) {
			standardServices.put(alias, new NeonService(alias, NeonPhpType.create(phpType)));
		}
	}

	private void addExtensionPattern(@NotNull Pattern pattern, @NotNull NeonExtensionItem extension) {
		standardExtensionPatterns.put(pattern, extension);
		extension.addPattern(pattern);
	}

	private void addExtensionItemAlias(@NotNull NeonExtensionItem extension, @NotNull NeonExtensionItem realExtension) {
		standardExtensionAliases.put(extension.chain.withChildKey(extension.name).toString(), realExtension);
		addExtensionItem(extension);
	}

	private NeonExtensionItem addExtensionItem(@NotNull String name) {
		return addExtensionItem(name, false);
	}

	private NeonExtensionItem addExtensionItem(@NotNull String name, @NotNull NeonPhpType phpType) {
		NeonExtensionItem extension = new NeonExtensionItem(name, phpType);
		return addExtensionItem(extension, false, false);
	}

	private NeonExtensionItem addExtensionItem(@NotNull String name, NeonExtensionItem.Version maxVersion) {
		return addExtensionItem(name, maxVersion, false, false);
	}

	private NeonExtensionItem addExtensionItem(@NotNull String name, boolean patternOnly, boolean isMain) {
		NeonExtensionItem extension = new NeonExtensionItem(name);
		return addExtensionItem(extension, patternOnly, isMain);
	}

	private NeonExtensionItem addExtensionItem(@NotNull String name, @NotNull NeonExtensionItem.Version maxVersion, boolean patternOnly, boolean isMain) {
		NeonExtensionItem extension = new NeonExtensionItem(name);
		extension.setMaxVersion(maxVersion);
		return addExtensionItem(extension, patternOnly, isMain);
	}

	private NeonExtensionItem addExtensionItem(@NotNull String name, boolean patternOnly) {
		NeonExtensionItem extension = new NeonExtensionItem(name);
		return addExtensionItem(extension, patternOnly);
	}

	private NeonExtensionItem addExtensionItem(@NotNull NeonExtensionItem extension, boolean patternOnly) {
		return addExtensionItem(extension, patternOnly, false);
	}

	private NeonExtensionItem addExtensionItem(@NotNull NeonExtensionItem extension) {
		return addExtensionItem(extension, false, false);
	}

	private NeonExtensionItem addExtensionItem(@NotNull NeonExtensionItem extension, boolean patternOnly, boolean isMain) {
		Map<String, NeonExtensionItem> currentMap;
		extension.setPatternOnly(patternOnly);

		String key;
		if (isMain) {
			key = extension.chain.withChildKey(extension.name).toString();
		} else {
			key = extension.chain.toString();
		}

		if (patternOnly) {
			if (isMain) {
				return extension;
			}

			if (!patternsOnlyExtensions.containsKey(key)) {
				currentMap = new HashMap<>();
				patternsOnlyExtensions.put(key, currentMap);
			}
			patternsOnlyExtensions.get(key).put(extension.name, extension);

		} else {
			if (!standardExtensions.containsKey(key)) {
				currentMap = new HashMap<>();
				standardExtensions.put(key, currentMap);
			}
			standardExtensions.get(key).put(extension.name, extension);
		}
		return extension;
	}

	private NeonPhpType type(String type) {
		return NeonPhpType.create(type);
	}

}
