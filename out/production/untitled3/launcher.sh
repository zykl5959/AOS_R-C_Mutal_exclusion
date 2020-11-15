#!/bin/bash

# Your netid
NETID=yxz180009
# name of the directory when the project is located
PROJECT_DIRECTORY="$HOME/RAM/src"
# name of the configuration file
CONFIG_FILE="$PROJECT_DIRECTORY/configuration.txt"
# name of the program to be run
PROGRAM_FILE=LockTest

# initialize iteration variable
i=0
# read the configuration file
# replace any phrase starting with "#" with an empty string and then delete any empty lines
cat "$CONFIG_FILE" | sed -e "s/#.*//" | sed "/^$/d" |
{	
	# read the number of nodes
	read n
	Node_num=$( echo $n | awk '{ print $1 }' )
	Inter_request_delay=$( echo $n | awk '{ print $2 }' )
	Cs_execution_time=$( echo $n | awk '{ print $3 }' )
	Node_requests=$( echo $n | awk '{ print $4 }' )
	echo "system contains" $Node_num "nodes"
	# read the location of each node one by one
	while [ $i -lt $Node_num ]
	do
		# read a line
		read line
		# echo $line
		# extract the node identifier
		IDENTIFIER=$( echo $line | awk '{ print $1 }' )
		# extract the machine name
		HOST=$( echo $line | awk '{ print $2 }' )
		PORT=$( echo $line | awk '{ print $3 }' )
		echo "spawning node" $IDENTIFIER on "machine" $HOST "port" $PORT
		# construct the string specifying the program to be run by the ssh command
		ARGUMENTS="java -classpath \"$PROJECT_DIRECTORY\" $PROGRAM_FILE $IDENTIFIER $PORT $Inter_request_delay $Cs_execution_time $Node_requests $Node_num"
		echo $ARGUMENTS
		# spawn the node
		# any error message will be stored in the log files
		xterm -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $NETID@$HOST '$ARGUMENTS' 2> log.launcher.$IDENTIFIER"   &
		i=$((i+1)) 
	done
}
