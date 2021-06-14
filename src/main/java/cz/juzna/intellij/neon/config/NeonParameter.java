package cz.juzna.intellij.neon.config;

import cz.juzna.intellij.neon.util.NeonPhpType;
import org.jetbrains.annotations.NotNull;

public class NeonParameter {

	/** extension name, e.g. 'tempDir' */
	private final String name;

	private NeonPhpType phpType;

	public NeonParameter(@NotNull String name, @NotNull NeonPhpType phpType) {
		this.name = name;
		this.phpType = phpType;
	}

	public NeonParameter(@NotNull String name) {
		this(name, NeonPhpType.MIXED);
	}

	public String getName() {
		return name;
	}

	public NeonPhpType getPhpType() {
		return phpType;
	}

	public void setPhpType(NeonPhpType phpType) {
		this.phpType = phpType;
	}

}