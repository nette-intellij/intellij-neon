package cz.juzna.intellij.neon.util;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import cz.juzna.intellij.neon.psi.NeonFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class NeonPsiUtil {
	public static <T> List<T> acceptAllFiles(FileType fileType, Project project, Class<T> tClass, Predicate<? super T> predicate) {
		List<T> results = new ArrayList<T>();
		acceptAllFiles(fileType, new PsiRecursiveElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				if (tClass.isInstance(element) && predicate.test((T) element)) {
					results.add((T) element);
				} else {
					super.visitElement(element);
				}
			}
		}, project);
		return results;
	}

	public static void acceptAllFiles(FileType fileType, PsiElementVisitor recursiveVisitor, Project project) {
		Collection<VirtualFile> virtualFiles =
				FileTypeIndex.getFiles(fileType, GlobalSearchScope.allScope(project));
		for (VirtualFile virtualFile : virtualFiles) {
			NeonFile simpleFile = (NeonFile) PsiManager.getInstance(project).findFile(virtualFile);
			if (simpleFile != null) {
				for (PsiElement element : simpleFile.getChildren()) {
					element.acceptChildren(recursiveVisitor);
				}
			}
		}
	}

}
