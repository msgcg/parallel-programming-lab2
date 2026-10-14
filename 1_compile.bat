@echo off

chcp 65001 > nul

setlocal

echo ====================================================================

echo   КОМПИЛЯЦИЯ ИСХОДНОГО КОДА: ЛАБОРАТОРНАЯ РАБОТА №2

echo   Потоки и процессы в языке Java (GUI + Многопоточный бенчмарк)

echo   Автор: Эрденко М. Д. (ПО-32б) -- Преподаватель: Степаков В. И.

echo ====================================================================

set "JAVA_BIN=java"
set "JAVAC_BIN=javac"
if exist "C:\Users\maxxf\AppData\Local\Programs\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe" (
set "JAVA_BIN=C:\Users\maxxf\AppData\Local\Programs\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe"
set "JAVAC_BIN=C:\Users\maxxf\AppData\Local\Programs\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\javac.exe"
)
if exist "C:\Users\m7036\tools\jdk-21\bin\java.exe" (
set "JAVA_BIN=C:\Users\m7036\tools\jdk-21\bin\java.exe"
set "JAVAC_BIN=C:\Users\m7036\tools\jdk-21\bin\javac.exe"
)


echo.

echo Запуск компиляции MultiThreadApp.java с кодировкой UTF-8...

"%JAVAC_BIN%" -encoding UTF-8 MultiThreadApp.java

if %errorlevel% equ 0 (

echo.

echo [УСПЕХ] Файлы классов успешно скомпилированы.

echo.

echo Теперь вы можете запустить:

echo   - "2_Запуск_GUI_Окна.bat" для графической демонстрации

echo   - "3_Запуск_Бенчмарка_CLI.bat" для замера ускорения вычислений

echo   - "4_Инспекция_Потоков_jstack.bat" для снятия дампа потоков JVM

) else (

echo.

echo [ОШИБКА] Во время компиляции произошла ошибка , код ошибки: %errorlevel%.

)

echo.

pause


