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

import antlr.ANTLRException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.transform.TransformerException;
import org.bn.compiler.parser.ASNLexer;
import org.bn.compiler.parser.ASNParser;
import org.bn.compiler.parser.model.ASN1Model;
import org.bn.compiler.parser.model.ASNModule;

import org.apache.commons.cli.*;

public class Main {

    private static final String VERSION = "1.6";
    
    private CompilerArgs arguments = new CompilerArgs();

    public static void main(String args[]) {
        try {
            System.out.println("BinaryNotes compiler v" + VERSION);
            System.out.println("        (c) 2006-2015 Abdulla G. Abdurakhmanov, Pavel Drasil");
            new Main().start(args);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public void start(String[] args) throws Exception {
        Options options = new Options();

        Option moduleOption = Option.builder("m")
                .longOpt("moduleName")
                .hasArg()
                .desc("Binding module name ('cs' or 'java')")
                .required()
                .build();
        options.addOption(moduleOption);
        Option outputDirOption = Option.builder("o")
                .longOpt("outputDir")
                .hasArg()
                .desc("Output directory name")
                .optionalArg(true)
                .build();
        options.addOption(outputDirOption);
        Option namespaceOption = Option.builder("ns")
                .longOpt("namespace")
                .hasArg()
                .desc("Generate classes with specified namespace/package name")
                .optionalArg(true)
                .build();
        options.addOption(namespaceOption);
        Option generateModelOnlyOption = Option.builder("x")
                .longOpt("model-only")
                .desc("Generate only the ASN.1 model (as XML)")
                .optionalArg(true)
                .build();
        options.addOption(generateModelOnlyOption);
        Option helpOption = Option.builder("h")
                .longOpt("help")
                .desc("Show help message")
                .build();
        options.addOption(helpOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        String executable = "bncompiler-" + VERSION + ".jar";

        try {
            CommandLine cmd = parser.parse(options, args);
             // Show help and exit if --help is used
            if (cmd.hasOption("h")) {
                printHelp(formatter, options, executable);
                return;
            }

            arguments.setModuleName( cmd.getOptionValue("m") );
            arguments.setOutputDir( cmd.getOptionValue("o") );
            arguments.setNamespace( cmd.getOptionValue("ns") );
            arguments.setGenerateModelOnly( cmd.hasOption("x") );
            arguments.setInputFileName( cmd.getArgs() ); // Remaining arguments

            if (arguments.getInputFileNames() == null || arguments.getInputFileNames().length == 0) {
                throw new ParseException("No input files specified.");
            }

            Module module = new Module(arguments.getModuleName(), arguments.getOutputDir());
            startForModule(module);
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            printHelp(formatter, options, executable);
            System.exit(1);
        }
    }

    private static void printHelp(HelpFormatter formatter, Options options, String executable) {
        String usage = executable + " --moduleName <cs|java> --outputDir <output_dir> -ns <namespace> <file1> [file2] ...";
        String header = "\nParses a list of files for a specified animal type.\n\nOptions:";
        String footer = "\nExample:\n  " + executable + "--moduleName cs --outputDir output_ns -ns test_asn test.asn\n\n"
                + "  " + executable + " --moduleName java --outputDir output_java -ns test_asn test.asn\n";
        formatter.printHelp(usage, header, options, footer, false);
    }

    private void startForModule(Module module) throws TransformerException, JAXBException, IOException, ANTLRException {
        if (!arguments.getGenerateModelOnly()) {
            System.out.println("Current directory: " + new File(".").getCanonicalPath());
            System.out.println("Compiling file(s): " + String.join(", ", arguments.getInputFileNames()));
            
            ByteArrayOutputStream outputXml = new ByteArrayOutputStream(65535);
            createModel(outputXml, module);
            module.translate(new ByteArrayInputStream(outputXml.toByteArray()));
        } else {
            createModel(System.out, null);
        }
    }
    
    private void createModel(OutputStream outputXml, Module module) throws JAXBException, FileNotFoundException, ANTLRException {
        ASN1Model model = createModelFromStream();
        if (module != null) {
            model.outputDirectory = module.getOutputDir();
            if (arguments.getNamespace() != null) {
                model.moduleNS = arguments.getNamespace();
            } else {
                for (ASNModule m : model.modules) {
                    if (m.moduleIdentifier != null && m.moduleIdentifier.name != null) {
                        model.moduleNS = m.moduleIdentifier.name.toLowerCase();
                        break;
                    }
                }
            }
        }
        
        JAXBContext jc = JAXBContext.newInstance("org.bn.compiler.parser.model");
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(model, outputXml);
    }

    private ASN1Model createModelFromStream() throws FileNotFoundException, ANTLRException {
        ASN1Model model = new ASN1Model();
        model.modules = new java.util.ArrayList<>();
        for (String inputFile : arguments.getInputFileNames()) {
            InputStream stream = new FileInputStream(inputFile);
            ASNLexer lexer = new ASNLexer(stream);
            ASNParser parser = new ASNParser(lexer);

            ASNModule module = new ASNModule();
            parser.module_definition(module);

            model.modules.add(module);    
        }
        
        return model;
    }
}
