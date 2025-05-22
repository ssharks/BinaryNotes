package org.bn.compiler.parser.model;

import java.math.BigInteger;

public class AsnNamedNumber {
    
    public AsnDefinedValue definedValue;
    public boolean         isSignedNumber;
    public String          name;
    public AsnSignedNumber signedNumber;

    public BigInteger value() {
        if (signedNumber == null) {
            return null;
        }
        if (signedNumber.positive) {
            return signedNumber.num;
        } else {
            return signedNumber.num.negate();
        }   
    }

    public AsnNamedNumber() {
        name = "";
    }

    @Override
    public String toString() {
        String ts = name + "\t(";

        if (isSignedNumber) {
            ts += signedNumber;
        } else {
            ts += definedValue;
        }

        ts += ")";

        return ts;
    }
}
