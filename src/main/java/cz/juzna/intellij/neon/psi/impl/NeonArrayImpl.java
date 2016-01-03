package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import cz.juzna.intellij.neon.psi.NeonArray;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import cz.juzna.intellij.neon.psi.NeonValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

/**
 *
 */
public class NeonArrayImpl extends NeonPsiElementImpl implements NeonArray {

	private static Pattern number = Pattern.compile("^\\d+$");

	public NeonArrayImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon array";
	}

	@Override
	public boolean isList() {
		for (PsiElement el : getChildren()) {
			if (el instanceof NeonKeyValPair) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isHashMap() {
		return !isList();
	}

	@Override
	public List<NeonValue> getValues() {
		ArrayList<NeonValue> result = new ArrayList<NeonValue>();
		for (PsiElement el : getChildren()) {
			if (el instanceof NeonKeyValPair) {
				result.add(((NeonKeyValPair) el).getValue());
			} else {
				result.add(el.getChildren().length > 0 ? (NeonValue) el.getChildren()[0] : null);
			}
		}
		return result;
	}

	@Override
	public List<NeonKey> getKeys() {
		ArrayList<NeonKey> result = new ArrayList<NeonKey>();
		for (PsiElement el : getChildren()) {
			if (el instanceof NeonKeyValPair) {
				result.add(((NeonKeyValPair) el).getKey());
			}
		}

		return result;
	}

	@Override
	public HashMap<String, NeonValue> getMap() {
		HashMap<String, NeonValue> result = new LinkedHashMap<String, NeonValue>();
		Integer key = 0;
		for (PsiElement el : getChildren()) {
			if (el instanceof NeonKeyValPair) {
				NeonKeyValPair keyVal = (NeonKeyValPair) el;
				String keyText = keyVal.getKeyText();
				result.put(keyText, ((NeonKeyValPair) el).getValue());
				if (number.matcher(keyText).matches()) {
					Integer keyInt = Integer.parseInt(keyText);
					key = Math.max(keyInt + 1, key);
				}
			} else {
				String stringKey = (key++).toString();
				result.put(stringKey, el.getChildren().length > 0 ? (NeonValue) el.getChildren()[0] : null);
			}
		}

		return result;
	}

}
