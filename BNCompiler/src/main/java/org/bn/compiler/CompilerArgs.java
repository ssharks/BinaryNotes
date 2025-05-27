/*
 Copyright 2006-2011 Abdulla Abdurakhmanov (abdulla@latestbit.com)
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.bn.compiler;

public class CompilerArgs {

    private String moduleName = null;

    private String outputDir = "output/";

    private String[] inputFileNames = null;

    private String namespace = null;

    private Boolean generateModelOnly = false;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String[] getInputFileNames() {
        return inputFileNames;
    }

    public void setInputFileName(String[] inputFileNames) {
        this.inputFileNames = inputFileNames;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Boolean getGenerateModelOnly() {
        return generateModelOnly;
    }

    public void setGenerateModelOnly(Boolean generateModelOnly) {
        this.generateModelOnly = generateModelOnly;
    }
}
