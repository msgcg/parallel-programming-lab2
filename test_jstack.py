import subprocess
import re
import sys
import os
import shutil

def find_jdk_tool(tool_name):
    path = shutil.which(tool_name)
    if path:
        return path
    java_home = os.environ.get("JAVA_HOME", "")
    if java_home and os.path.exists(os.path.join(java_home, "bin", f"{tool_name}.exe")):
        return os.path.join(java_home, "bin", f"{tool_name}.exe")
    for base in [
        r"C:\Users\maxxf\AppData\Local\Programs\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin",
        r"C:\Users\m7036\tools\jdk-21\bin"
    ]:
        candidate = os.path.join(base, f"{tool_name}.exe")
        if os.path.exists(candidate):
            return candidate
    return tool_name

def inspect_jvm_threads():
    try:
        jps_bin = find_jdk_tool("jps")
        jstack_bin = find_jdk_tool("jstack")
        res = subprocess.run([jps_bin, "-l"], capture_output=True, text=True, check=True)
        pid = None
        for line in res.stdout.splitlines():
            if "MultiThreadApp" in line:
                pid = line.split()[0]
                break
                
        if not pid:
            print("Процесс MultiThreadApp не найден через jps. Убедитесь, что приложение запущено.")
            return

        print(f"Найден процесс MultiThreadApp (PID: {pid}). Выполняется снятие дампа потоков...")
        stack_res = subprocess.run([jstack_bin, pid], capture_output=True, text=True, check=True)

        target_threads = ["Thread-Rectangles", "Thread-Ovals", "Runnable-Lines"]
        print("\n=== СОСТОЯНИЕ ЦЕЛЕВЫХ ВЫЧИСЛИТЕЛЬНЫХ ПОТОКОВ ===")
        found = False
        for line in stack_res.stdout.splitlines():
            if any(t in line for t in target_threads):
                print(line)
                found = True
            elif found and "java.lang.Thread.State" in line:
                print("   " + line.strip())
                found = False

        print("\nДиагностика успешно выполнена.")
    except Exception as e:
        print(f"Ошибка при работе с jstack: {e}")

if __name__ == "__main__":
    inspect_jvm_threads()
