package cz.juzna.intellij.neon.file;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.SingleRootFileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * disables incremental reparsing
 */
public class NeonFileViewProvider extends SingleRootFileViewProvider {
	/*** boilerplate code ***/
	public NeonFileViewProvider(@NotNull PsiManager psiManager, @NotNull VirtualFile virtualFile) {
		super(psiManager, virtualFile);
	}

	public NeonFileViewProvider(@NotNull PsiManager psiManager, @NotNull VirtualFile virtualFile, boolean b) {
		super(psiManager, virtualFile, b);
	}

	public NeonFileViewProvider(@NotNull PsiManager psiManager, @NotNull VirtualFile virtualFile, boolean b, @NotNull Language language) {
		super(psiManager, virtualFile, b, language);
	}


	@Override
	public boolean supportsIncrementalReparse(@NotNull Language language) {
		return false;
	}
}
