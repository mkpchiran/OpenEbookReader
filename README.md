# OpenEbookReader
Open Source Free platform independent more interactive Ebook , ePub reader.

To install the OpenEbookReader You have to download liceance.jar from https://jxbrowser.support.teamdev.com/support/solutions/articles/9000012859-licensing and add it in to your local mvn repo by running

mvn install:install-file -Dfile=path to license.jar -DgroupId=com.teamdev.jxbrowser -DartifactId=license -Dversion=6.0 -Dpackaging=jar

to run the project

mvn install
mvn package
cd /target
java -jar OpenEbookReader-0.0.1-SNAPSHOT-jar-with-dependencies.jar
