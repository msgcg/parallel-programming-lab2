@echo off

chcp 65001 > nul

setlocal

echo ====================================================================

echo   ЗАПУСК ГРАФИЧЕСКОГО ПРИЛОЖЕНИЯ SWING (ПОТОКИ JAVA)

echo   Лабораторная работа №2 по дисциплине «Параллельное программирование»

echo   Автор: Эрденко М. Д. (ПО-32б) -- Преподаватель: Степаков В. И.

echo ====================================================================

echo.

echo В окне приложения:

echo   - 3 параллельных потока: Thread-Rectangles, Thread-Ovals, Runnable-Lines

echo   - Интерактивные кнопки управления: СТАРТ, ПАУЗА, ПРИОРИТЕТЫ

echo   - Панель статуса и счетчик отрисованных графических примитивов

echo ====================================================================

echo.

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


if not exist "MultiThreadApp.class" (

echo [ИНФО] Файл MultiThreadApp.class не найден. Запуск компиляции...

call "1_Скомпилировать.bat"

)

echo Запуск MultiThreadApp...

"%JAVA_BIN%" -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 MultiThreadApp

echo.

echo [ИНФО] Окно приложения закрыто.

pause


