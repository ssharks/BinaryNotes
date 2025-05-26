import zipfile
import os
import subprocess

zip_path = '../../BNCompiler/target/bncompiler-1.6-dist.zip'
test_asn_path = '../../BNCompiler/src/test/resources/test.asn'
cam_asn_path = '../../BNCompiler/src/test/resources/ITS_CAM_v1.3.2.asn'
extract_to = 'bncompiler'

os.makedirs(extract_to, exist_ok=True)

path = os.path.dirname(os.path.abspath(__file__));

print(path)

with zipfile.ZipFile(os.path.join(path, zip_path), 'r') as zip_ref:
    zip_ref.extractall(os.path.join(path,extract_to))

java = "java"
if os.environ.get('JAVA_HOME') != None :
    java = os.path.join(os.environ.get('JAVA_HOME'),java)

jar_path = os.path.join(path, extract_to, 'bncompiler-1.6.jar')

def run_compiler(namespace, asn_path, output_path):
    # works with JAVA 20
    java_options = ['-Dsun.misc.URLClassPath.disableJarChecking=true',
        '--add-opens', 'jdk.naming.rmi/com.sun.jndi.rmi.registry=ALL-UNNAMED',
        '--add-opens', 'java.base/java.lang=ALL-UNNAMED',
        '--add-opens', 'java.base/sun.security.action=ALL-UNNAMED',
        '--add-opens', 'java.base/sun.net=ALL-UNNAMED']

    cmd = [java]
    cmd.extend(java_options)

    # add the specific options for the bncompiler
    bn_compiler_args = [
        '-classpath', jar_path,
        'org.bn.compiler.Main',
        '-m', 'cs',
        '-ns', namespace, 
        '-o', output_path,
        "-f", asn_path
    ]
    
    cmd.extend(bn_compiler_args)

    result = subprocess.run(cmd, capture_output=True, text=True)
    #result = subprocess.run(cmd + ["-x"], capture_output=True, text=True)
    print(result.stdout)
    print(result.stderr)

    for root, _, files in os.walk(output_path):
        for file in files:
            file_path = os.path.join(root, file)
            with open(file_path, 'rb') as f:
                content = f.read()
            content = content.replace(b'\r\n', b'\n')
            with open(file_path, 'wb') as f:
                f.write(content)

run_compiler("org.bn.coders.test_asn", os.path.join(path, test_asn_path), os.path.join(path, 'org/bn/coders/test_asn'))
run_compiler("its.cam", os.path.join(path, cam_asn_path), os.path.join(path, 'org/bn/coders/cam_asn'))
