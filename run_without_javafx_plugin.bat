@echo off
echo Compilation du projet...
call mvn clean compile

echo Lancement de l'application...
call mvn exec:java -Dexec.mainClass="org.example.MainApp"

pause
