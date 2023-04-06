package cz.juzna.intellij.neon;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

public class NeonBundle {
	@NonNls
	private static final String BUNDLE_NAME = "messages.NeonBundle";
	@NotNull private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private NeonBundle() {
	}

	public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
		return AbstractBundle.message(BUNDLE, key, params);
	}
}
