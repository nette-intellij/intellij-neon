package cz.juzna.intellij.neon.reference;

import com.intellij.psi.util.PsiTreeUtil;
import cz.juzna.intellij.neon.file.NeonFileType;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.*;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

public class NeonElementFactory {
	public static NeonClassUsage createClassUsage(Project project, String name) {
		final NeonFile file = createKeyWithValue(project, "FooNamespace\\" + name);
		NeonClassUsage firstChild = PsiTreeUtil.findChildOfType(file, NeonClassUsage.class);
		if (firstChild != null) {
			try {
				return firstChild;

			} catch (NullPointerException e) {
				return null;
			}
		}
		return null;
	}

	public static NeonKey createKey(Project project, String name) {
		final NeonFile file = createKeyElement(project, name);
		NeonKeyValPair[] array = new NeonKeyValPair[1];
		file.acceptChildren(new PsiRecursiveElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				if (element instanceof NeonKeyValPair && ((NeonKeyValPair) element).getKey() != null && !((NeonKeyValPair) element).getKey().equals("foo")) {
					array[0] = (NeonKeyValPair) element;
				} else {
					super.visitElement(element);
				}
			}
		});
		if (array[0] == null) {
			return null;
		}

		NeonKey foundKey = array[0].getKey();
		if (foundKey != null) {
			try {
				if (foundKey.getLastChild().getNode().getElementType() == NeonTokenTypes.NEON_COLON) {
					foundKey.getNode().removeChild(foundKey.getLastChild().getNode());
				}
				return foundKey;

			} catch (NullPointerException e) {
				return null;
			}
		}
		return null;
	}

	public static NeonClassUsage createClassConstructUsage(Project project, String name) {
		final NeonFile file = createKeyWithValue(project, "FooNamespace\\" + name + "()");
		NeonClassUsage firstChild = PsiTreeUtil.findChildOfType(file, NeonClassUsage.class);
		if (firstChild != null) {
			try {
				return firstChild;

			} catch (NullPointerException e) {
				return null;
			}
		}
		return null;
	}

	public static NeonClassUsage createClassRootUsage(Project project, String name) {
		final NeonFile file = createKeyWithValue(project, "\\" + name);
		NeonClassUsage firstChild = PsiTreeUtil.findChildOfType(file, NeonClassUsage.class);
		if (firstChild != null) {
			try {
				return firstChild;

			} catch (NullPointerException e) {
				return null;
			}
		}
		return null;
	}

	public static NeonNamespaceReference createNamespaceReference(Project project, String name) {
		final NeonFile file = createKeyWithValue(project, name + "\\FooClass");
		NeonNamespaceReference firstChild = PsiTreeUtil.findChildOfType(file, NeonNamespaceReference.class);
		if (firstChild != null) {
			try {
				return firstChild;

			} catch (NullPointerException e) {
				return null;
			}
		}
		return null;
	}

	public static NeonParameterUsage createNeonParameterUsage(Project project, String name) {
		final NeonFile file = createKeyWithValue(project, "%" + name + "%");
		NeonParameterUsage firstChild = PsiTreeUtil.findChildOfType(file, NeonParameterUsage.class);
		if (firstChild != null) {
			try {
				return firstChild;

			} catch (NullPointerException e) {
				return null;
			}
		}
		return null;
	}

	public static NeonMethodUsage createMethodUsage(Project project, String name) {
		final NeonFile file = createKeyWithValue(project, name + "()");
		NeonMethodUsage firstChild = PsiTreeUtil.findChildOfType(file, NeonMethodUsage.class);
		if (firstChild != null) {
			try {
				return firstChild;

			} catch (NullPointerException e) {
				return null;
			}
		}
		return null;
	}

	private static NeonFile createKeyWithValue(Project project, String text) {
		String name = "dummy.neon";
		return (NeonFile) PsiFileFactory.getInstance(project).
				createFileFromText(name, NeonFileType.INSTANCE, "foo: " + text);
	}

	private static NeonFile createKeyElement(Project project, String key) {
		String name = "dummy.neon";
		return (NeonFile) PsiFileFactory.getInstance(project).
				createFileFromText(name, NeonFileType.INSTANCE, "foo:\n  " + key + ": val");
	}
}