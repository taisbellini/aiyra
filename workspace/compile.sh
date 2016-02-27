#!/bin/bash

pushd JavaCCPajeParser/src/br/ufrgs/inf/tlbellini/
javacc PajeGrammar.jj
popd
pushd JavaCCPajeParser/src/
javac br/ufrgs/inf/tlbellini/lib/*.java
javac br/ufrgs/inf/tlbellini/*.java
popd
echo
export CLASSPATH=`pwd`/JavaCCPajeParser/src/
echo "Execute the program by typing:"
echo "$ java br.ufrgs.inf.tlbellini.PajeGrammar <path to input Paje tracefile>"
