#!/bin/sh
#
# Copyright (c) 2004, 2010, Oracle and/or its affiliates. All rights reserved. 

# Build: 110804

DIRNAME=`dirname $0`

PATH=/bin:/usr/bin:/sbin:/usr/sbin:/usr/local/bin
export PATH

if [ "-getver" = "$1" ]
 then
  EXECTASK_OUT_FILE="$DIRNAME/exectask.$$.out"
  $DIRNAME/exectask "$@" > $EXECTASK_OUT_FILE 2>&1
  EXIT_STATUS=$?
  out=`cat $EXECTASK_OUT_FILE`
  if [ "$EXIT_STATUS" = "0" ]
   then
    echo $out
   else
    echo "<CV_CMD>$0 $@</CV_CMD><CV_VAL>$out</CV_VAL><CV_ERES>$EXIT_STATUS</CV_ERES><CV_ERR></CV_ERR>"
  fi
  rm -rf $EXECTASK_OUT_FILE
else
  exec $DIRNAME/exectask "$@" 
fi
