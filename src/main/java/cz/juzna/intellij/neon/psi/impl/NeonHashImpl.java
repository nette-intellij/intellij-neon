package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import cz.juzna.intellij.neon.parser.NeonElementTypes;
import cz.juzna.intellij.neon.psi.NeonHash;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonKeyValPair;
import cz.juzna.intellij.neon.psi.NeonValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class NeonHashImpl extends NeonPsiElementImpl implements NeonHash {
	public NeonHashImpl(@NotNull ASTNode astNode) {
		super(astNode);
	}

	public String toString() {
		return "Neon hash";
	}


	@Override
	public List<NeonKeyValPair> getItems() {
		ArrayList<NeonKeyValPair> result = new ArrayList<NeonKeyValPair>();

		for (ASTNode node : getNode().getChildren(TokenSet.create(NeonElementTypes.KEY_VALUE_PAIR))) {
			NeonKeyValPair pair = (NeonKeyValPair) node.getPsi();
			result.add(pair);
		}

		return result;
	}

	@Override
	public List<NeonKey> getKeys() {
		ArrayList<NeonKey> result = new ArrayList<NeonKey>();
		for (NeonKeyValPair pair : getItems()) result.add(pair.getKey());
		return result;
	}

	@Override
	public HashMap<String, NeonValue> getMap() {
		HashMap<String, NeonValue> result = new HashMap<String, NeonValue>();
		for (NeonKeyValPair pair : getItems()) result.put(pair.getKeyText(), pair.getValue());
		return result;
	}

}
