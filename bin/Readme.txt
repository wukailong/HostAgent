Change /META-INF/MANIFEST.MF

Manifest-Version: 1.0
Archiver-Version: Plexus Archiver
Created-By: Apache Maven
Built-By: wukailong
Build-Jdk: 1.7.0_45
Class-Path: lib/commons-codec-1.4.jar
Main-Class: com.scis.licensemanager.LicenseGenerator


Class-Path: lib/commons-codec-1.1.jar lib/commons-httpclient-3.0.1.jar lib/httpclient-4.2.1.jar lib/jackson-jaxrs-1.9.0.jar lib/sigar-1.6.4.jar lib/jackson-mapper-asl-1.9.0.jar lib/jackson-core-asl-1.9.0.jar lib/httpcore-4.2.1.jar lib/commons-logging-1.1.1.jar
Main-Class: com.host.node.MainController



java -jar -Djava.library.path=.\libdll HostAgent-1.jar



如果有多个jar包需要引用的情况：

Class-Path: lib/some.jar lib/some2.jar
