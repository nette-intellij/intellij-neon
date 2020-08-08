package cz.juzna.intellij.neon.util;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NeonPhpType {

	final private static String[] nativeTypeHints = new String[]{"string", "int", "bool", "object", "float", "array", "mixed", "null", "callable", "iterable"};

	final public static NeonPhpType MIXED = new NeonPhpType("mixed");

	private final List<TypePart> types = new ArrayList<TypePart>();
	private final boolean nullable;
	private boolean hasClass = false;

	private NeonPhpType(String type) {
		this(type, false);
	}

	private NeonPhpType(String typeString, boolean nullable) {
		if (typeString == null || typeString.length() == 0) {
			types.add(new TypePart("mixed"));

		} else {
			String[] parts = typeString.split("\\|");
			for (String part : parts) {
				part = part.trim();
				if (part.length() == 0) {
					continue;
				}

				String lower = part.toLowerCase();
				if (lower.equals("null")) {
					nullable = true;
					continue;
				}

				TypePart typePart = new TypePart(part);
				if (typePart.isClass) {
					this.hasClass = true;
				}
				types.add(typePart);
			}
		}
		this.nullable = nullable;
	}

	public static NeonPhpType create(String type) {
		if (type == null || type.length() == 0 || type.toLowerCase().equals("mixed")) {
			return MIXED;
		}
		return new NeonPhpType(type);
	}

	public boolean containsClasses() {
		return hasClass;
	}

	public boolean hasUndefinedClass(@NotNull Project project) {
		if (!containsClasses()) {
			return false;
		}

		for (String className : findClasses()) {
			if (NeonPhpUtil.getClassesByFQN(project, className).size() == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean hasClass(String className) {
		if (!containsClasses()) {
			return false;
		}
		String normalizedName = NeonPhpUtil.normalizeClassName(className);
		return types.stream().anyMatch(typePart -> typePart.isClass && typePart.getPart().equals(normalizedName));
	}

	public boolean hasClass(Collection<PhpClass> phpClasses) {
		if (!containsClasses()) {
			return false;
		}
		for (PhpClass phpClass : phpClasses) {
			if (hasClass(phpClass.getFQN())) {
				return true;
			}
		}
		return false;
	}

	public boolean isNullable() {
		return nullable;
	}

	public Collection<PhpClass> getPhpClasses(Project project) {
		List<PhpClass> output = new ArrayList<>();
		for (String wholeType : findClasses()) {
			output.addAll(NeonPhpUtil.getClassesByFQN(project, wholeType));
		}
		return output;
	}

	String[] findClasses() {
		if (!containsClasses()) {
			return new String[0];
		}
		return types.stream()
				.filter(typePart -> typePart.isClass)
				.map(TypePart::getPart)
				.toArray(String[]::new);
	}

	@Override
	public String toString() {
		return toReadableString();
	}

	public String toReadableString() {
		String out =  types.stream()
				.map(TypePart::getPart)
				.collect(Collectors.joining("|"));
		if (nullable) {
			out += "|null";
		}
		return out;
	}

	public static boolean isNativeTypeHint(@NotNull String value) {
		value = value.toLowerCase();
		return Arrays.asList(nativeTypeHints).contains(value.startsWith("\\") ? value.substring(1) : value);
	}

	static class TypePart {
		String part;
		boolean isClass = false;
		boolean isNative = false;
		boolean isArrayOf = false;

		TypePart (@NotNull String part) {
			if (part.endsWith("[]")) {
				part = "array";
				this.isArrayOf = true; //todo: add support for types in array

			} else if (isNativeTypeHint(part)) {
				part = part.toLowerCase();
				this.isNative = true;

			} else {
				part = NeonPhpUtil.normalizeClassName(part);
				this.isClass = true;
			}
			this.part = part;
		}

		String getPart() {
			return part;
		}
	}

}