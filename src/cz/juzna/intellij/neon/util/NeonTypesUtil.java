package cz.juzna.intellij.neon.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class NeonTypesUtil {

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

	public static Map<String, NeonPhpType> getPhpSessionDirectives() {
		return phpSessionDirectives;
	}

	public static boolean isPhpSessionDirective(@NotNull String value) {
		return phpSessionDirectives.containsKey(value.trim().toLowerCase());
	}

}