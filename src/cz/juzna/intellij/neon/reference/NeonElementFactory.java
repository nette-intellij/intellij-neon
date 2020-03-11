package cz.juzna.intellij.neon.reference;

import com.intellij.psi.util.PsiTreeUtil;
import cz.juzna.intellij.neon.file.NeonFileType;
import cz.juzna.intellij.neon.psi.NeonClassUsage;
import cz.juzna.intellij.neon.psi.NeonFile;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import cz.juzna.intellij.neon.psi.NeonMethodUsage;
import cz.juzna.intellij.neon.psi.NeonNamespaceReference;

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

	public static NeonNamespaceReference createClassRootUsage(Project project, String name) {
		final NeonFile file = createKeyWithValue(project, "\\" + name);
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
}