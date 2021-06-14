package cz.juzna.intellij.neon.config;

import cz.juzna.intellij.neon.util.NeonPhpType;
import org.jetbrains.annotations.NotNull;

public class NeonService {

	/** extension name, e.g. 'tempDir' */
	private final String name;

	private NeonPhpType phpType;

	private String[] aliases;

	public NeonService(@NotNull String name, @NotNull NeonPhpType phpType, @NotNull String[] aliases) {
		this.name = name;
		this.phpType = phpType;
		this.aliases = aliases;
	}

	public NeonService(@NotNull String name, @NotNull NeonPhpType phpType) {
		this(name, phpType, new String[0]);
	}

	public NeonService(@NotNull String name) {
		this(name, NeonPhpType.MIXED);
	}

	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public NeonPhpType getPhpType() {
		return phpType;
	}

	public void setPhpType(NeonPhpType phpType) {
		this.phpType = phpType;
	}

}