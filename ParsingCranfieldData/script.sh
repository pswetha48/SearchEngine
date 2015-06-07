#!bin/bash
clear
echo "Welcome"
echo
echo "Navigate to the ParsingCranfieldData folder if you still haven't."
echo
echo "I will list the directories in this folder"
echo
red='\033[0;31m'
NC='\033[0m' # No Color
echo "${red}"
ls
echo "${NC}"
echo
echo "Do you see the src folder? y/n"
read response 
if [ "$response" = "y" ] 
then
	echo "Running program..."
	javac -d bin -classpath bin:libs/jsoup-1.8.1.jar src/parsing/*.java
	java -classpath bin:libs/jsoup-1.8.1.jar parsing.Main /home/shruthi/AllFiles/Sem2/InfoRetrieval/Project/Cranfield
	echo "Thank you!"
else 
	echo "Please navigate to the ParsingCranfield folder and try again."
fi

java -classpath bin:libs/jsoup-1.8.1.jar:/home/shruthi/AllFiles/Sem2/InfoRetrieval/corenlp341/*.jar:/home/shruthi/AllFiles/Sem2/InfoRetrieval/corenlp341/ejml-0.23-src.zip Index

java -classpath bin:/home/shruthi/AllFiles/Sem2/InfoRetrieval/corenlp341/*.jar:/home/shruthi/AllFiles/Sem2/InfoRetrieval/corenlp341/ejml-0.23-src.zip:libs/jsoup-1.8.1.jar Index
