@echo off
cd /d "%~dp0"
echo Compiling...
javac -d out -sourcepath . -cp "lib\*" jp/ac/kyoto_u/kueps/STube/STube.java jp/ac/kyoto_u/kueps/naruse_utils/MiscUtils.java jp/ac/kyoto_u/kueps/naruse_utils/NFontChooser.java jp/ac/kyoto_u/kueps/naruse_utils/PSExporter.java gnu/io/CommPort.java gnu/io/CommPortIdentifier.java gnu/io/NoSuchPortException.java gnu/io/PortInUseException.java gnu/io/SerialPort.java gnu/io/UnsupportedCommOperationException.java
if %errorlevel% equ 0 (
    echo Compilation completed successfully!
) else (
    echo Compilation failed with errors.
)
pause
