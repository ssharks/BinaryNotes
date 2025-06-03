call ../bncompiler.cmd -m java -o output/test -ns org.bn.coders.test_asn test.asn
javac -cp ../../../../JavaLibrary/target/binarynotes-1.6.jar output/test/*.java
