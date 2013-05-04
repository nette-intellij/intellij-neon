package cz.juzna.intellij.neon.completion.schema;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.*;
import gnu.trove.THashMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Reads schema definition and finds available keys and values for completion
 */
public class SchemaProvider {

	public static final String NAMESPACE_NAME = "\\NEON_META\\";

	/**
	 * Public interface, wraps native impl in cache
	 */
	synchronized public Map<String, Collection<String>> getKnownTypes(Project project) {
		// TODO: add cache
		return parseTypes(project);
	}


	/**
	 * Reads annotations and metadata and produces a cacheable map
	 */
	private Map<String, Collection<String>> parseTypes(Project project) {
		Map<String, Collection<String>> map = new THashMap<String, Collection<String>>(); // parent -> known-key[]
		Collection<Variable> variables = getVariables(project, "CONFIG_KEYS");
		for (Variable variable : variables) {
			if ( ! NAMESPACE_NAME.equals(variable.getNamespaceName())) continue;
			PsiElement parent = variable.getParent();

			if (parent instanceof AssignmentExpression) {
				PhpPsiElement value = ((AssignmentExpression)parent).getValue();
				if (value instanceof ArrayCreationExpression) {
					Iterable<ArrayHashElement> elements = ((ArrayCreationExpression) value).getHashElements();
					parseTypes2(map, elements, "");
				}
			}
		}
		return map;
	}

	private void parseTypes2(Map<String, Collection<String>> map, Iterable<ArrayHashElement> elements, String parent) {
		Collection<String> types = map.get(parent);
		if (types == null) {
			types = new ArrayList<String>();
			map.put(parent, types);
		}

		for (ArrayHashElement element : elements) {
			PhpPsiElement key = element.getKey();
			if (key instanceof StringLiteralExpression) {
				// key
				String keyName = ((StringLiteralExpression) key).getContents();
				types.add(keyName);

				String fullKeyName = parent.length() > 0 ? (parent + "." + keyName) : keyName;
				
				// value
				PhpPsiElement val = element.getValue();
				if (val instanceof ArrayCreationExpression) { // recursive
					Iterable<ArrayHashElement> subElements = ((ArrayCreationExpression) val).getHashElements();
					parseTypes2(map, subElements, fullKeyName);

				} else if(val instanceof FieldReference) { // reference to a field, where it's defined
					String classFqn = ((ClassReference) ((FieldReference) val).getClassReference()).getFQN();
					for(PhpClass phpClass : PhpIndex.getInstance(element.getProject()).getClassesByFQN(classFqn)) {
						Field field = phpClass.findFieldByName(((FieldReference) val).getNameCS(), false);
						if (field.getDefaultValue() instanceof ArrayCreationExpression) {
							Iterable<ArrayHashElement> subElements = ((ArrayCreationExpression) field.getDefaultValue()).getHashElements();
							parseTypes2(map, subElements, fullKeyName);
						}
					}

				} else { // get value type
					parseValueType(val);

					// try annotation
					PsiElement el2 = element;
					while (el2 != null && (el2 instanceof LeafPsiElement && ((LeafPsiElement) el2).getElementType() == PhpTokenTypes.opCOMMA || el2 instanceof PsiWhiteSpace)) {
						el2 = el2.getNextSibling();
					}
					if (el2 instanceof PsiComment) {
						System.out.println("Comment for " + fullKeyName + ": " + el2.getText());
					}

				}
			}
		}
	}

	private void parseValueType(PhpPsiElement val) {
		int x = 1;
	}

	private Collection<Variable> getVariables(Project project, String key) {
		return PhpIndex.getInstance(project).getVariablesByName(key);
	}

	private PsiFile getMetaFile(Project project) {
		VirtualFile metaFile = LocalFileSystem.getInstance().findFileByPath(project.getBasePath() + File.separatorChar + ".phpstorm.meta.php");
		return metaFile != null ? PsiManager.getInstance(project).findFile(metaFile) : null;
	}

}
