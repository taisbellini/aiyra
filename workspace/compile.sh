#!/bin/bash

pushd JavaCCPajeParser/src/br/ufrgs/inf/tlbellini/
javacc PajeGrammar.jj
popd
pushd JavaCCPajeParser/src/
export CLASSPATH=../..//include/mysql-connector-java-5.1.38-bin.jar:.
javac br/ufrgs/inf/tlbellini/lib/*.java
javac br/ufrgs/inf/tlbellini/*.java
popd
export CLASSPATH=`pwd`/JavaCCPajeParser/src/
export CLASSPATH=$CLASSPATH:/Users/taisbellini/TCC/aiyra/workspace/include/mysql-connector-java-5.1.38-bin.jar:.
echo "Execute the program by typing:"
echo "$ java br.ufrgs.inf.tlbellini.PajeGrammar <path to input Paje tracefile>"
