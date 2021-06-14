package cz.juzna.intellij.neon.config;

import cz.juzna.intellij.neon.util.NeonPhpType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class NeonExtensionItem {

	/** extension name, e.g. 'session' */
	public final String name;

	@NotNull
	public final NeonKeyChain chain;

	public final Version version;

	@Nullable
	public Version maxVersion;

	public NeonPhpType phpType;

	public boolean patternOnly;

	private List<String> possibleValues = new ArrayList<>();

	private final List<Pattern> patterns = new ArrayList<>();

	private final List<Tag> tags = new ArrayList<Tag>();

	private final List<NeonExtensionItem> childrens = new ArrayList<>();

	public NeonExtensionItem(
			@NotNull String name,
			@NotNull NeonKeyChain chain,
			@NotNull Version version,
			@NotNull NeonPhpType phpType,
			boolean patternOnly
	) {
		this.name = name;
		this.version = version;
		this.chain = chain;
		this.patternOnly = patternOnly;
		this.phpType = phpType;
		this.maxVersion = null;
	}

	public NeonExtensionItem(@NotNull String name, @NotNull String[] chain, @NotNull Version version) {
		this(name, NeonKeyChain.get(chain), version, NeonPhpType.MIXED, false);
	}

	public NeonExtensionItem(@NotNull String name, @NotNull NeonKeyChain chain, @NotNull NeonPhpType phpType) {
		this(name, chain, Version.V_2_4, phpType, false);
	}

	public NeonExtensionItem(@NotNull String name, @NotNull NeonPhpType phpType) {
		this(name, NeonKeyChain.get(new String[0]), Version.V_2_4, phpType, false);
	}

	public NeonExtensionItem(@NotNull String name, @NotNull NeonKeyChain chain) {
		this(name, chain, Version.V_2_4, NeonPhpType.MIXED, false);
	}

	public NeonExtensionItem(@NotNull String name, @NotNull NeonKeyChain chain, @NotNull Version version) {
		this(name, chain, version, NeonPhpType.MIXED, false);
	}

	public NeonExtensionItem(@NotNull String name, @NotNull Version version) {
		this(name, NeonKeyChain.get(new String[0]), version, NeonPhpType.MIXED, false);
	}

	public NeonExtensionItem(@NotNull String name) {
		this(name, new String[0], Version.V_2_4);
	}

	public void setMaxVersion(@Nullable Version maxVersion) {
		this.maxVersion = maxVersion;
	}

	public NeonPhpType getPhpType() {
		return phpType;
	}

	public void setPhpType(NeonPhpType phpType) {
		this.phpType = phpType;
	}

	public void addTag(Tag tag) {
		if (this.tags.contains(tag)) {
			this.tags.add(tag);
		}
	}

	public boolean hasTag(Tag tag) {
		return this.tags.contains(tag);
	}

	public List<String> getPossibleValues() {
		return Collections.unmodifiableList(possibleValues);
	}

	public void addPossibleValues(String[] values) {
		for (String value : values) {
			if (!possibleValues.contains(value)) {
				possibleValues.add(value);
			}
		}
	}

	public NeonExtensionItem addChild(@NotNull String childName, @NotNull Version version) {
		NeonExtensionItem child = new NeonExtensionItem(childName, chain.withChildKey(name), version);
		addChild(child);
		return child;
	}

	public NeonExtensionItem addChild(@NotNull String childName) {
		NeonExtensionItem child = new NeonExtensionItem(childName, chain.withChildKey(name));
		addChild(child);
		return child;
	}

	public NeonExtensionItem addChild(@NotNull String childName, @NotNull NeonPhpType phpType) {
		NeonExtensionItem child = new NeonExtensionItem(childName, chain.withChildKey(name), phpType);
		addChild(child);
		return child;
	}

	public void setPatternOnly(boolean patternOnly) {
		this.patternOnly = patternOnly;
	}

	public boolean isPatternOnly() {
		return patternOnly;
	}

	public void addChild(@NotNull NeonExtensionItem child) {
		childrens.add(child);
	}

	public void addPattern(@NotNull Pattern pattern) {
		patterns.add(pattern);
	}

	public List<NeonExtensionItem> getChildrens() {
		return childrens;
	}

	public List<Pattern> getPatterns() {
		return patterns;
	}

	public enum Version {
		V_2_3("< 2.4"),
		V_2_4("< 3.0"),
		V_3("3.0");

		private String translate;

		Version(@NotNull String translate) {
			this.translate = translate;
		}

		public String getTranslate() {
			return translate;
		}
	}

	public enum Tag {
		SETUP_METHODS
	}

}