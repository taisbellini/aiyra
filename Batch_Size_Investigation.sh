#!/bin/bash

# CPP_BatchSize_Investigation.sh

# For each line
  # get the name of the program (column 5)
  # get the input size (column 6)
  # Launch the experiment, get execution time
  # print the line, with the time.

function usage()
{
  echo "./Cpp_Batch_Size_Investigation.sh <design> <platform>";
}


DESIGN=$1
if [ -z "$DESIGN" ]; then
    usage;
    exit;
fi
PLATFORM=$2
if [ -z "$PLATFORM" ]; then
    usage;
    exit
fi

while read -r line; do
    INPUTSIZE=`echo $line | cut -d, -f5 | sed -e "s/\"//g"`
    BATCHSIZE=`echo $line | cut -d, -f6 | sed -e "s/\"//g"`
    EXEC=" "
    if [[ $INPUTSIZE = "input" ]]; then
       echo $line
       continue
    fi
    if [[ $INPUTSIZE = "small" ]]; then
      INPUT="trace-128K.paje"
      MAXBATCH=2727
    elif [[ $INPUTSIZE = "medium" ]]; then
      INPUT="trace-128M.paje"
      MAXBATCH=2526628
    elif [[ $INPUTSIZE = "big" ]]; then 
      INPUT="trace-1G.paje"
      MAXBATCH=19570722
    fi
    if [[ $BATCHSIZE = "A" ]]; then 
      BATCH=$MAXBATCH
    elif [[ $BATCHSIZE = "B" ]]; then
      BATCH=$((MAXBATCH/2))
    elif [[ $BATCHSIZE = "C" ]]; then
      BATCH=$((MAXBATCH/4))
    elif [[ $BATCHSIZE = "D" ]]; then
      BATCH=$((MAXBATCH/8))
    elif [[ $BATCHSIZE = "E" ]]; then
      BATCH=$((MAXBATCH/16))
    elif [[ $BATCHSIZE = "F" ]]; then
      BATCH=$((MAXBATCH/32))
    fi
    EXEC="$(java br.ufrgs.inf.tlbellini.PajeGrammar -f $INPUT -p mysql -batch $BATCH -plat guarani -test)"
    TIME=`echo $EXEC | cut -d" " -f18`
    TIME_INSERTION=`echo $EXEC | cut -d" " -f8`
    MAX_MEM=`echo $EXEC | cut -d" " -f10`
    if [[ $MAX_MEM = "exceeded" ]]; then
      EXEC=NA
      TIME=NA 
      TIME_INSERTION=NA
      MAX_MEM=NA
    fi
    echo $line | cut -d, -f1-6 | tr '\n' ','
    echo "$TIME,$TIME_INSERTION,$MAX_MEM,$PLATFORM"
done < "$DESIGN"
