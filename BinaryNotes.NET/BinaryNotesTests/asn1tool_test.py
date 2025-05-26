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
		extendedInt1	[3] INTEGER (0..63) OPTIONAL,
		extendedInt2	[4] INTEGER (0..100000)
	}

ChoiceType ::= CHOICE {
    field10	[0] INTEGER,
    field20	[1] OCTET STRING,
    ...,
    field30	[2] UTF8String,
    field40	[3] INTEGER
}

ExtendedChoiceSeq ::= SEQUENCE {
    choi      ChoiceType,
    tail      INTEGER (0..255)
}

ExtensibleSize ::= SEQUENCE (SIZE(1..3,...)) OF INTEGER(0..63)
ExtensibleInteger ::= INTEGER(0..63,...)

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
        uper_encoded = self.uper_compiler.encode(entry, data)
        print("UPER: " + uper_encoded.hex())
        print("UPER decoded" + self.uper_compiler.decode(entry, uper_encoded).__str__())
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

data5 = {
    "choi" : ('field10', 0x12 ),
    'tail': 0x19
}
# encoding 1 + 1 + 8 + 8 = 18 bits

data6 = {
    "choi" : ('field40', 0x12 ),
    'tail': 0x19
}

compiler.compile('ExtendedChoiceSeq', data5)
compiler.compile('ExtendedChoiceSeq', data6)

data7 = 0x23

data8 = 0x73

compiler.compile('ExtensibleInteger', data7)
compiler.compile('ExtensibleInteger', data8)