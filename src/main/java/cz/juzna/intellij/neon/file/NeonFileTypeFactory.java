/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: BashFileTypeLoader.java, Class: BashFileTypeLoader
 * Last modified: 2010-03-24
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.juzna.intellij.neon.file;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;


public class NeonFileTypeFactory extends FileTypeFactory {
	public void createFileTypes(@NotNull FileTypeConsumer consumer) {
		consumer.consume(NeonFileType.NEON_FILE_TYPE, NeonFileType.DEFAULT_EXTENSION);
	}
}
