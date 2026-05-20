@echo off
setlocal

:: Force JDK 25 for JavaFX
set JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-25.0.1.8-hotspot"
set PATH="%JAVA_HOME%\bin";%PATH%

:: Verify Java version (MUST show JDK 25.0.1)
echo Using Java for JavaFX:
java -version

:: Navigate to your project directory
cd /d "C:\Users\Hp-NoteBook\NotepadApp"

:: Run JavaFX with the CORRECT module path (use the first path from dir /s)
java --module-path "C:/openjfx-25.0.1_windows-x64_bin-sdk/javafx-sdk-25.0.1/lib" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics --enable-native-access=javafx.graphics -cp bin;lib/* App

pause
endlocal