$ErrorActionPreference = 'Stop'
$projectDirectory = Split-Path $PSScriptRoot -Parent
Push-Location $projectDirectory
try {
    if (-not $env:JAVA_HOME) { throw 'Set JAVA_HOME to a JDK 17 or newer installation.' }
    & "$env:JAVA_HOME\bin\javac.exe" --module-path 'target/javafx-base-21.0.7-win.jar;target/javafx-graphics-21.0.7-win.jar;target/javafx-controls-21.0.7-win.jar' --add-modules javafx.controls -encoding UTF-8 -cp target/classes -d target/visual-test tools/VisualSmoke.java
    if ($LASTEXITCODE -ne 0) { throw 'Visual check compilation failed. Run mvn package -P!win-package first.' }
    & "$env:JAVA_HOME\bin\java.exe" "-Djavafx.cachedir=$projectDirectory\target\javafx-cache" --module-path 'target/javafx-base-21.0.7-win.jar;target/javafx-graphics-21.0.7-win.jar;target/javafx-controls-21.0.7-win.jar' --add-modules javafx.controls -cp 'target/*;target/visual-test' VisualSmoke
    if ($LASTEXITCODE -ne 0) { throw 'Visual verification failed.' }
} finally { Pop-Location }
