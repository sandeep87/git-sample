#!/bin/sh
OHOME=%ORACLE_HOME%
INVPTRLOC=$OHOME/oraInst.loc
ADDNODE="$OHOME/oui/bin/runInstaller -addNode -invPtrLoc $INVPTRLOC ORACLE_HOME=$OHOME $*"
if [ "$IGNORE_PREADDNODE_CHECKS" = "Y" -o ! -f "$OHOME/cv/cvutl/check_nodeadd.pl" ]
then
	$ADDNODE
else
        CHECK_NODEADD="$OHOME/perl/bin/perl $OHOME/cv/cvutl/check_nodeadd.pl -pre $*"
        $CHECK_NODEADD
        if [ $? -eq 0 ]
        then
	$ADDNODE
	fi
fi
