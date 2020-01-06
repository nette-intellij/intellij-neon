package cz.juzna.intellij.neon.reference;

import com.intellij.psi.PsiElement;
import cz.juzna.intellij.neon.file.NeonFileType;
import cz.juzna.intellij.neon.psi.NeonFile;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

public class NeonElementFactory {
	public static PsiElement createClassType(Project project, String name) {
		final NeonFile file = createKeyWithValue(project, name);
		PsiElement firstChild = file.getFirstChild();
		if (firstChild != null) {
			try {
				return firstChild.getFirstChild().getLastChild();

			} catch (NullPointerException e) {
				return null;
			}
		}
		return null;
	}

	public static NeonFile createKeyWithValue(Project project, String text) {
		String name = "dummy.neon";
		return (NeonFile) PsiFileFactory.getInstance(project).
				createFileFromText(name, NeonFileType.INSTANCE, "foo: " + text);
	}
}