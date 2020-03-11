package cz.juzna.intellij.neon.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NeonTypesUtil {

	final private static String[] excludedCompletion = new String[]{"__construct", "__callstatic", "__call", "__get", "__isset", "__clone", "__set", "__unset"};

	final private static Map<String, NeonPhpType> phpSessionDirectives = new HashMap<String, NeonPhpType>(){{
		put("session.save_handler", NeonPhpType.create("string"));
		put("session.gc_probability", NeonPhpType.create("int"));
		put("session.gc_divisor", NeonPhpType.create("int"));
		put("session.gc_maxlifetime", NeonPhpType.create("int"));
		put("session.serialize_handler", NeonPhpType.create("string"));
		put("session.cookie_httponly", NeonPhpType.create("bool"));
		put("session.cache_limiter", NeonPhpType.create("string"));
		put("session.cache_expire", NeonPhpType.create("int"));
		put("session.sid_length", NeonPhpType.create("int"));
		put("session.sid_bits_per_character", NeonPhpType.create("int"));
		put("session.hash_function", NeonPhpType.create("mixed"));
		put("session.hash_bits_per_character", NeonPhpType.create("int"));
		put("session.entropy_file", NeonPhpType.create("string"));
		put("session.entropy_length", NeonPhpType.create("int"));
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