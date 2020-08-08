package cz.juzna.intellij.neon.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NeonTypesUtil {

	final private static String[] excludedCompletion = new String[]{"__construct", "__callstatic", "__call", "__get", "__isset", "__clone", "__set", "__unset"};

	final private static Map<String, NeonPhpType> phpSessionDirectives = new HashMap<String, NeonPhpType>(){{
		put("saveHandler", NeonPhpType.create("string"));
		put("gcProbability", NeonPhpType.create("int"));
		put("gcDivisor", NeonPhpType.create("int"));
		put("gcMaxlifetime", NeonPhpType.create("int"));
		put("serializeHandler", NeonPhpType.create("string"));
		put("cookieHttponly", NeonPhpType.create("bool"));
		put("cacheLimiter", NeonPhpType.create("string"));
		put("cacheExpire", NeonPhpType.create("int"));
		put("sidLength", NeonPhpType.create("int"));
		put("sidBitsPerCharacter", NeonPhpType.create("int"));
		put("hashFunction", NeonPhpType.create("mixed"));
		put("hashBitsPerCharacter", NeonPhpType.create("int"));
		put("entropyFile", NeonPhpType.create("string"));
		put("entropyLength", NeonPhpType.create("int"));
	}};

	final private static Map<String, NeonPhpType> phpDirectives = new HashMap<String, NeonPhpType>(){{
		put("date.timezone", NeonPhpType.create("string"));
		put("zlib.output_compression", NeonPhpType.create("bool"));
		put("short_open_tag", NeonPhpType.create("bool"));
		put("file_uploads", NeonPhpType.create("bool"));
		put("max_execution_time", NeonPhpType.create("int"));
		put("max_input_time", NeonPhpType.create("int"));
		put("memory_limit", NeonPhpType.create("string|int"));
		put("post_max_size", NeonPhpType.create("string|int"));
		put("upload_max_filesize", NeonPhpType.create("string|int"));
		put("include_path", NeonPhpType.create("string"));
		put("disable_functions", NeonPhpType.create("string"));
		put("register_globals", NeonPhpType.create("bool"));
		put("allow_url_fopen", NeonPhpType.create("bool"));
		put("allow_url_include", NeonPhpType.create("bool"));
	}};

	public static Map<String, NeonPhpType> getPhpSessionDirectives() {
		return phpSessionDirectives;
	}

	public static Map<String, NeonPhpType> getPhpDirectives() {
		return phpDirectives;
	}

	public static boolean isPhpSessionDirective(@NotNull String value) {
		return phpSessionDirectives.containsKey(value.trim().toLowerCase());
	}

	public static boolean isExcludedCompletion(@NotNull String value) {
		return Arrays.asList(excludedCompletion).contains(value.toLowerCase());
	}

}