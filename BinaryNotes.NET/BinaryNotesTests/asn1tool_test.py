# Example: Encode data using ASN.1 UPER with asn1tools

import asn1tools

# Define a simple ASN.1 specification
asn1_spec = """
MyModule DEFINITIONS AUTOMATIC TAGS ::= 
BEGIN

ProtectedZoneType ::= ENUMERATED {
    permanentCenDsrcTolling(0),
    ...,
    temporaryCenDsrcTolling(1)
}

ExtendedEnumSeq ::= SEQUENCE {
    prot      ProtectedZoneType,
    tail      INTEGER (0..255)
}

DataSeqExtensible   ::= SEQUENCE {
		simpleInt		[0] INTEGER (0 .. 255),
		simpleBool  	[1] BOOLEAN,
		optBool			[2] BOOLEAN OPTIONAL,
		...,
		extendedInt1	[3] INTEGER (0..63),
		extendedInt2	[4] INTEGER (0..63)
	}	

END
"""

class Compiler:
    def __init__(self, asn1_spec):
        # Compile the ASN.1 specification for UPER
        self.uper_compiler = asn1tools.compile_string(asn1_spec, 'uper')
        self.per_compiler = asn1tools.compile_string(asn1_spec, 'per')
        self.ber_compiler = asn1tools.compile_string(asn1_spec, 'ber')

    def compile(self, entry, data):
        # Compile the ASN.1 specification
        print(f"{entry}: {data}")
        print("UPER: " + self.uper_compiler.encode(entry, data).hex())
        print("PER: " + self.per_compiler.encode(entry, data).hex())
        print("BER: " + self.ber_compiler.encode(entry, data).hex())
        
compiler = Compiler(asn1_spec)

# Data to encode
data1 = {
    'prot': "permanentCenDsrcTolling",
    'tail': 0x19
}

data2 = {
    'prot': "temporaryCenDsrcTolling",
    'tail': 0x19
}

# Encode the data
compiler.compile('ExtendedEnumSeq', data1)
compiler.compile('ExtendedEnumSeq', data2)

data3 = {
    'simpleInt': 0x45,
    'simpleBool': True,
    'extendedInt2': 0x2D
}

data4 = {
    'simpleInt': 0x45,
    'simpleBool': True,
    'optBool': True,
    'extendedInt1': 0x11,
    'extendedInt2': 0x2D
}

compiler.compile('DataSeqExtensible', data3)
compiler.compile('DataSeqExtensible', data4)
