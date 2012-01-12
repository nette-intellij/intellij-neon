/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: BashFileType.java, Class: BashFileType
 * Last modified: 2010-06-30
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


import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.LanguageFileType;
import cz.juzna.intellij.neon.Neon;
import cz.juzna.intellij.neon.NeonIcons;
import cz.juzna.intellij.neon.NeonLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


public class NeonFileType extends LanguageFileType {
    private static final Logger LOG = Logger.getInstance("#NeonFileType");
    public static final NeonFileType NEON_FILE_TYPE = new NeonFileType();


    public static final String DEFAULT_EXTENSION = "neon";


    /**
     * All extensions which are associated with this plugin.
     */
    public static final String[] extensions = { DEFAULT_EXTENSION };


    protected NeonFileType() {
        super(NeonLanguage.LANGUAGE);
    }

    @NotNull
    public String getName() {
        return Neon.languageName;
    }

    @NotNull
    public String getDescription() {
        return Neon.languageDescription;
    }

    @NotNull
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public Icon getIcon() {
        return NeonIcons.FILETYPE_ICON;
    }

    @Override
    public boolean isJVMDebuggingSupported() {
        return false;
    }
}

