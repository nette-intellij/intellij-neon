package cz.juzna.intellij.neon.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NeonKeyChain {

	private final String[] chain;

	@Nullable
	private final String stringKey;

	private static final Map<String, NeonKeyChain> chains = new HashMap<>();

	private NeonKeyChain(@NotNull String[] chain) {
		this.chain = chain;
		this.stringKey = NeonKeyChain.toString(chain);
	}

	public static NeonKeyChain get(@NotNull String mainKey) {
		return get(new String[]{mainKey});
	}

	public static NeonKeyChain get(@NotNull String[] chain) {
		String chainString = NeonKeyChain.toString(chain);
		if (NeonKeyChain.chains.containsKey(chainString)) {
			return NeonKeyChain.chains.get(chainString);
		}

		NeonKeyChain keyChain = new NeonKeyChain(chain);
		NeonKeyChain.chains.put(keyChain.toString(), keyChain);
		return keyChain;
	}

	public NeonKeyChain withChildKey(@NotNull String childKey) {
		List<String> list = new ArrayList<String>(Arrays.asList(chain));
		list.add(childKey);
		return new NeonKeyChain(list.toArray(new String[0]));
	}

	public boolean equalsMainKey(@NotNull String firstKey) {
		return chain.length > 0 && chain[0].equals(firstKey);
	}

	public boolean equalsWithoutMainKeyWithDots(@NotNull String firstKey) {
		List<String> list = new ArrayList<String>(Arrays.asList(chain));
		if (list.size() > 0) {
			list.remove(0);
		}
		return String.join(".", list).equals(firstKey);
	}

	public NeonKeyChain getParentChain() {
		List<String> list = new ArrayList<String>(Arrays.asList(chain));
		if(list.size() > 0) {
			list.remove(list.size() - 1);
		}
		return new NeonKeyChain(list.toArray(new String[0]));
	}

	public NeonKeyChain withoutParentKey() {
		List<String> list = new ArrayList<String>(Arrays.asList(chain));
		if (list.size() > 0) {
			list.remove(0);
		}
		return new NeonKeyChain(list.toArray(new String[0]));
	}

	public boolean isLevel(int level) {
		return chain.length == level;
	}

	@Override
	public String toString() {
		return stringKey;
	}

	private static String toString(String[] chain) {
		return String.join("|", Arrays.asList(chain));
	}

	public String toDottedString() {
		return String.join(".", Arrays.asList(chain));
	}

	public String[] toArray() {
		return chain;
	}
}
