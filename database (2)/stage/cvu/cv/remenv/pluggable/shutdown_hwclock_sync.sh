#!/bin/sh
#
# $Header: opsm/cvutl/pluggable/shutdown_hwclock_sync.sh /main/1 2010/04/22 01:46:59 narbalas Exp $
#
# shutdown_hwclock_sync.sh
#
# Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved. 
#
#    NAME
#      shutdown_hwclock_sync.sh - <one-line expansion of the name>
#
#    DESCRIPTION
#      Checks whether the hardware clock is synced to the system clock at shutdown
#
#    NOTES
#      <other useful comments, qualifications, etc.>
#
#    MODIFIED   (MM/DD/YY)
#    narbalas    04/01/10 - Initial Version
#    narbalas    04/01/10 - Creation
#
#This function returns the location of the shutdown script based on the Linux distribution
getshutdownscript()
{
  $RPM -q enterprise-release >/dev/null
  if [ $? -eq 0 ]
  then
      #Current OS is OEL
      SHUTDOWNSCRIPT=/etc/rc.d/init.d/halt
      return  
  fi
  $RPM -q redhat-release >/dev/null
  if [ $? -eq 0 ]
  then
      #Current OS is RHEL 
      SHUTDOWNSCRIPT=/etc/rc.d/init.d/halt
      return  
  fi
  $RPM -q sles-release >/dev/null
  if [ $? -eq 0 ]
  then
      #SUSE it is
      SHUTDOWNSCRIPT=/etc/init.d/halt.local
      return
  fi
  #Current OS is not supported.Report and bail out
  ERRCODE=4
  frameresult
  exitfromscript
}

#Leave the script gracefully
exitfromscript()
{
  echo $RESULT
  exit $ERRCODE
}

#Construct the result output of the script
frameresult()
{
  case $ERRCODE in
       0) 
          RESULT="<RESULT>SUCC</RESULT><COLLECTED>true</COLLECTED><TRACE>Hardware clock is correctly synchronized with system clock in the shutdown script on node $host</TRACE>"
          ;;
       1)
          RESULT="<RESULT>VFAIL</RESULT><COLLECTED>false</COLLECTED><TRACE>Check failed on node $host, hwclock command not present in $SHUTDOWNSCRIPT</TRACE>"
          ;;
       2)
          RESULT="<RESULT>WARN</RESULT><COLLECTED>false</COLLECTED><TRACE>Test could not determine conclusively hardware clock synchronization status at shutdown on node $host. User to validate manually </TRACE><NLS_MSG><FACILITY>Prve</FACILITY><ID>0027</ID><MSG_DATA><DATA>$host</DATA></MSG_DATA></NLS_MSG>"
          ;;
       3)
          RESULT="<RESULT>EFAIL</RESULT><COLLECTED>false</COLLECTED><TRACE>Command failed on node $host.File $SHUTDOWNSCRIPT does not exist</TRACE><NLS_MSG><FACILITY>Prve</FACILITY><ID>0028</ID><MSG_DATA><DATA>$host</DATA></MSG_DATA></NLS_MSG>"
          ;;
       4)
          RESULT="<RESULT>EFAIL</RESULT><COLLECTED>false</COLLECTED><TRACE>Command failed on node $host.The current distribution is not supported for this check</TRACE><NLS_MSG><FACILITY>Prve</FACILITY><ID>0029</ID><MSG_DATA><DATA>$host</DATA></MSG_DATA></NLS_MSG>"
          ;;
       5)
          RESULT="<RESULT>EFAIL</RESULT><COLLECTED>false</COLLECTED><TRACE>Command failed on node $host.No data found in $SHUTDOWNSCRIPT</TRACE><NLS_MSG><FACILITY>Prve</FACILITY><ID>0029</ID><MSG_DATA><DATA>$host</DATA></MSG_DATA></NLS_MSG>"
          ;;
       *) RESULT="<RESULT>UNKNOWN</RESULT><COLLECTED>false</COLLECTED><TRACE>Test for hardware clock synchronization on shutdown failed on node $host. </TRACE><NLS_MSG><FACILITY>Prve</FACILITY><ID>0029</ID><MSG_DATA><DATA>$host</DATA></MSG_DATA></NLS_MSG>"
          ;;
  esac
  return
}

#Check whether the command is indeed  "hwclock --systohc" uncommented
lookForPatternInSameLine()
{
  $GREP "hwclock" $SHUTDOWNSCRIPT | $GREP -v "^#" | $GREP "systohc" >/dev/null
  if [ $? -eq 0 ] 
  then
      # systohc option was found on the same line , pass the task
      ERRCODE=0
  fi
  checkerrcode
}

#If systohc option was not found in the same line , then it's possible to be elsewhere in the script.
#Check whether that is the case
#This is for situations where the hwclock cmd is aliased or contained in a shell variable in the shutdown script
#Searching below for a known standard pattern
checkForStandardPatterns()
{
  $GREP "CLOCKFLAGS[[:blank:]]*--systohc" $SHUTDOWNSCRIPT | grep -v "^#" > /dev/null
  if [ $? -eq 0 ]
  then 
      ERRCODE=0
  fi
  checkerrcode
}

#Check whether the hwclock and --systohc are concatneated in the script by other means. 
#That means they are present on different lines ,which the user should manually verify 
checkForMultipleLines()
{
  $GREP "hwclock" $SHUTDOWNSCRIPT | grep -v "^#" >/dev/null 2>&1
  if [ $? -eq 0 ]
  then
      # Search for "--systohc"
      $GREP "systohc" $SHUTDOWNSCRIPT >/tmp/blank.$$  2>&1
      if [ $? -eq 0 ]
      then
          #hwclock and --systohc are on different lines. Install user needs to manually check whether that works
          #Set result to a warning for user to check 
          ERRCODE=2
      else
          ERRCODE=1
      fi
  fi
  checkerrcode
}

checkerrcode()
{
  if [ $ERRCODE -lt 9 ]
  then
      frameresult
      exitfromscript
  fi
}

#Main - Script execution starts here
#Initialize variables
RPM=/bin/rpm 
RM=/bin/rm
GREP=/bin/grep
host=`/bin/hostname`
#End Initialize variables
ERRCODE=9
getshutdownscript
if [ -f $SHUTDOWNSCRIPT ]
then
    #do nothing#
    ERRCODE=9
else
    #Signal that file does not exist ,and exit
    ERRCODE=3
    checkerrcode
fi

checkForStandardPatterns
lookForPatternInSameLine
checkForMultipleLines
ERRCODE=5
frameresult
exitfromscript

#End of script

