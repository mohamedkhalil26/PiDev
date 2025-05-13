#!/bin/bash
echo "Compilation du projet..."
mvn clean compile

echo "Lancement de l'application..."
mvn exec:java -Dexec.mainClass="org.example.MainApp"
