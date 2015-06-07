#!bin/bash
clear
echo "Welcome"
echo
echo "Navigate to the RankedRetrieval folder if you still haven't."
echo
echo "I will list the directories in this folder"
echo
ls
echo
echo "Do you see the src folder? y/n"
read response 
if [ "$response" = "y" ] 
then
	echo "Running program..."
	javac -d bin -classpath bin:/usr/local/corenlp341/stanford-corenlp-3.4.1.jar:/usr/local/corenlp341/stanford-corenlp-3.4.1-models.jar src/*.java
	java -classpath bin:/usr/local/corenlp341/stanford-corenlp-3.4.1.jar:/usr/local/corenlp341/stanford-corenlp-3.4.1-models.jar Main
	echo "Thank you!"
else 
	echo "Please navigate to the RankedRetrieval folder and try again."
fi

