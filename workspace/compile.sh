#!/bin/bash

pushd JavaCCPajeParser/src/br/ufrgs/inf/tlbellini/
javacc PajeGrammar.jj
popd
pushd JavaCCPajeParser/src/
export CLASSPATH=../..//include/mysql-connector-java-5.1.38-bin.jar:../..//include/ibatis-sqlmap-2.3.0.jar:.
javac br/ufrgs/inf/tlbellini/lib/*.java
javac br/ufrgs/inf/tlbellini/*.java
popd
export CLASSPATH=`pwd`/JavaCCPajeParser/src/
export CLASSPATH=$CLASSPATH:/Users/taisbellini/TCC/aiyra/workspace/include/mysql-connector-java-5.1.38-bin.jar:.:`pwd`/./include/mysql-connector-java-5.1.38-bin.jar:`pwd`/./include/ibatis-sqlmap-2.3.0.jar:.
echo "Execute the program by typing:"
echo "$ java br.ufrgs.inf.tlbellini.PajeGrammar -f <path to input Paje tracefile> -p <plugin>"
