package cz.juzna.intellij.neon.file;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class NeonFileTypeFactory extends FileTypeFactory {
	public void createFileTypes(@NotNull FileTypeConsumer consumer) {
		consumer.consume(NeonFileType.INSTANCE, NeonFileType.DEFAULT_EXTENSION);
	}
}
