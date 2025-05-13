@echo off
echo Lancement de l'application...

REM Essayer d'abord avec javafx:run
echo Tentative avec javafx:run...
mvn clean javafx:run

IF %ERRORLEVEL% NEQ 0 (
    echo Tentative avec exec:java...
    mvn clean compile exec:java
)

pause
