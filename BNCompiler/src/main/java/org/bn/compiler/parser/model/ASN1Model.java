package org.bn.compiler.parser.model;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement public class ASN1Model implements Serializable {    
    public String    outputDirectory;
    public String    moduleNS;
    public java.util.ArrayList<ASNModule> modules = new java.util.ArrayList<ASNModule>();
}
