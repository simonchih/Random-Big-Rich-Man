@echo off
setlocal
set "RICHMAN_JAVA=java"
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "RICHMAN_JAVA=%JAVA_HOME%\bin\java.exe"
if not exist "%~dp0target\random-big-rich-man-1.0.0-SNAPSHOT.jar" (
  echo Build the game first: mvn package -P!win-package
  exit /b 1
)
if not exist "%~dp0target\javafx-controls-21.0.7-win.jar" (
  echo JavaFX dependencies are missing. Run: mvn package -P!win-package
  exit /b 1
)
"%RICHMAN_JAVA%" --module-path "%~dp0target\javafx-base-21.0.7-win.jar;%~dp0target\javafx-graphics-21.0.7-win.jar;%~dp0target\javafx-controls-21.0.7-win.jar" --add-modules javafx.controls -cp "%~dp0target\*" Game
exit /b %ERRORLEVEL%
