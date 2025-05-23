package org.bn.compiler.parser.model;

import java.util.ArrayList;
import java.math.BigInteger;

public class AsnNamedNumberList {
    
    public ArrayList<AsnNamedNumber> namedNumbers;
    public boolean isExtensible;
    public boolean sortOnAddition = false;
    public int numRootElements = 0;

    public AsnNamedNumberList() {
        namedNumbers = new ArrayList<>();
        numRootElements = 0;
    }

    public AsnNamedNumberList(boolean sortOnAddition) {
        namedNumbers = new ArrayList<>();
        this.sortOnAddition = sortOnAddition;
    }

    // sort by value when adding a new named number
    public void addNamedNumber(AsnNamedNumber namedNumber, boolean extendedElement) {
        
        if (!extendedElement) {
            numRootElements++;
            namedNumbers.add(namedNumber);
            if (sortOnAddition) {
                namedNumbers.sort((a, b) -> a.value().compareTo(b.value()));
            }
        } else {
            if (sortOnAddition) {
                // find the maximum value in the namedNumbers list by value
                BigInteger maxValue = namedNumbers.stream()
                        .map(AsnNamedNumber::value)
                        .max(BigInteger::compareTo)
                        .orElse(BigInteger.valueOf(Integer.MIN_VALUE));

                if (namedNumber.value().compareTo(maxValue) > 0) {
                    namedNumbers.add(namedNumber);
                } else {
                    throw new IllegalArgumentException("The new named number's value must be greater than all existing values in the list.");
                }
            }
        }
    }

    /** Returns the total number of elements in the list */
    public int count() {
        return namedNumbers.size();
    }
}
