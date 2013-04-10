#!/bin/sh
#
# $Header: opsm/cvutl/pluggable/checkmemlock.sh /main/2 2010/05/21 23:14:56 narbalas Exp $
#
# checkmemlock.sh
#
# Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved. 
#
#    NAME
#      checkmemlock.sh - <one-line expansion of the name>
#
#    DESCRIPTION
#      A script to validate whether max. memory locked limit is less than a certain value
#      Consumed by the pluggable framework as a Pluggable Task.
#
#    NOTES
#
#    MODIFIED   (MM/DD/YY)
#    narbalas    05/07/10 - Fix Expected Value
#    narbalas    04/22/10 - Creation
#


checkMemLock()
{
$GREP "memlock" $LIMITSDOTCONF | $GREP -v "^#" | $GREP "^\*" | $GREP hard >/dev/null 2>&1
if [ $? -eq 0 ]
then
    VAL=`$GREP "memlock" $LIMITSDOTCONF | $GREP  "^\*"  | $GREP 'hard' | $AWK '{ print $4 }' 2>&1`
    if [ $VAL -lt $EXPECTED ]
    then
        #fail check
        ERRCODE=1
        return
    else
       ERRCODE=0
       return
    fi
else
    ERRCODE=2
    return
fi
}

setupEnv()
{
EXPECTED=3145728
VAL=0
GREP=/bin/grep
LS=/bin/ls
AWK=/bin/awk
CAT=/bin/cat
LIMITSDOTCONF=/etc/security/limits.conf
MOUNT=/bin/mount
ERRCODE=9
CURUSR=`/usr/bin/whoami`
HOST=`/bin/hostname`
}

prepareResult()
{
 case $ERRCODE in
       0)
          RESULT="<RESULT>SUCC</RESULT><COLLECTED>true</COLLECTED><TRACE>Check for maximum memory locked limit when DB Automatic Memory Management feature is enabled passed on node $HOST</TRACE>"
          ;;
       1)
          RESULT="<RESULT>VFAIL</RESULT><COLLECTED>false</COLLECTED><TRACE>Check failed on node $HOST, Maximum locked memory limit is less than $EXPECTED</TRACE><NLS_MSG><FACILITY>Prve</FACILITY><ID>0042</ID><MSG_DATA><DATA>$EXPECTED</DATA><DATA>$VAL</DATA><DATA>$HOST</DATA></MSG_DATA></NLS_MSG>"
          ;;
       2)
          RESULT="<RESULT>VFAIL</RESULT><COLLECTED>false</COLLECTED><TRACE>Check failed on node $HOST, no values set for Maximum locked memory</TRACE><NLS_MSG><FACILITY>Prve</FACILITY><ID>0044</ID><MSG_DATA><DATA>$HOST</DATA></MSG_DATA></NLS_MSG>"
          ;;
       9) RESULT="<RESULT>EFAIL</RESULT><COLLECTED>false</COLLECTED><TRACE>Check for maximum memory locked limit when DB Automatic memory management feature is enabled encountered a command failure </TRACE><NLS_MSG><FACILITY>Prve</FACILITY><ID>0043</ID><MSG_DATA><DATA>$HOST</DATA></MSG_DATA></NLS_MSG>"
          ;;
  esac
  return
}

adieu()
{
  echo $RESULT
  exit 
}

#Main
#Setup environment
setupEnv
checkMemLock
prepareResult
adieu



