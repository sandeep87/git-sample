#!/bin/sh
#
# $Header: opsm/cvutl/pluggable/checkhugepage.sh /main/3 2010/05/21 23:14:56 narbalas Exp $
#
# checkhugepage.sh
#
# Copyright (c) 2009, 2010, Oracle and/or its affiliates. All rights reserved. 
#
#    NAME
#      checkhugepage.sh - <one-line expansion of the name>
#
#    DESCRIPTION
#      <short description of component this file declares/defines>
#
#    NOTES
#      <other useful comments, qualifications, etc.>
#
#    MODIFIED   (MM/DD/YY)
#    narbalas    05/06/10 - Fix script to ensure correct execution for tests
#    shmubeen    12/29/09 - check whether hugepages are set or not if available
#                           memory is >= 4GB
#    shmubeen    12/29/09 - Creation
#

SCAT="/bin/cat"
SGREP="/bin/grep"
SAWK="/bin/awk"
HUGEPAGEPATH="/proc/sys/vm/nr_hugepages"
REQMEMSIZE=4
HOST=`hostname`
MEMINGB=0
CHECKHUGEPAGES=0

if [ "${CVU_TEST_ENV}" = "true" ] &&  [ "X${CVU_TEST_REQ_MEM}" != "X" ] && [ "X${CVU_TEST_NR_HP}" != "X" ]
then
     REQMEMSIZE=${CVU_TEST_REQ_MEM}  
     HUGEPAGEPATH=${CVU_TEST_NR_HP}
fi
 
# Gets the number of huge pages.
# If /proc/sys/vm/nr_hugepages file does not exist, hugepages is not enabled.


# Gets available memory
# It is suggested to enable Huge pages if Available Memory is >=4GB
getAvailMem()
{
PHYSMEM=`$SCAT /proc/meminfo | $SGREP MemTotal | $SAWK '{print $2}'`
ret=$?
if [ $ret -eq 0 ]
then
    #Dividing  by 1048576 never gives the correct expression
    #Since the decision to check for enabled hugepages only depends on whether the system has 4GB or more 
    #The below expression can be used since  /proc/meminfo NEVER returns TotalMem as a multiple of 1048576
    MEMINGB=`expr $PHYSMEM / 1048576  + 1` 
else
    #Command Failure - Failed to get physical memory
    ERRCODE=2
    frameResult
    exit
fi
if [ $MEMINGB -ge $REQMEMSIZE ]    
then
    CHECKHUGEPAGES=1
    return
fi
return
}

frameResult()
{
  case $ERRCODE in
       0) RESULT="<RESULT>SUCC</RESULT><COLLECTED>true</COLLECTED><TRACE>Huge Pages feature is enabled on node $HOST</TRACE>"
          ;;
       1) RESULT="<RESULT>WARN</RESULT><COLLECTED>false</COLLECTED><TRACE>Huge Pages feature is not enabled on $HOST</TRACE><NLS_MSG><FACILITY>Prve</FACILITY><ID>0021</ID><MSG_DATA><DATA>$HOST</DATA></MSG_DATA></NLS_MSG>"
          ;;
       2) RESULT="<RESULT>WARN</RESULT><COLLECTED>false</COLLECTED><EXEC_ERROR>Error while getting physical memory of the system</EXEC_ERROR><TRACE>Unable to get the physical memory of the system</TRACE><NLS_MSG><FACILITY>Prve</FACILITY><ID>0022</ID><MSG_DATA><DATA>$HOST</DATA></MSG_DATA></NLS_MSG>"
          ;;
  esac
  return
}

checkHugePagesEnabled()
{
if [ -f $HUGEPAGEPATH ]
then
    #Success case
    ERRCODE=0
else
    ERRCODE=1    
fi
}

#Main
getAvailMem
if [ $CHECKHUGEPAGES -eq 1 ]
then
    checkHugePagesEnabled 
else    
    #Return Success
    ERRCODE=0
fi
frameResult
echo $RESULT
