import subprocess
import time

app_proc = subprocess.Popen(
    ["java", "-cp", "/home/maxim/Лабы/pp_labs/lab2", "MultiThreadApp"],
    cwd="/home/maxim/Лабы/pp_labs/lab2"
)

time.sleep(2)
pid = app_proc.pid

res = subprocess.run(["jstack", str(pid)], capture_output=True, text=True)
print("=== JSTACK OUTPUT ===")
for line in res.stdout.splitlines():
    if any(k in line for k in ["Thread-Rectangles", "Thread-Ovals", "Runnable-Lines", "RenderThreadGroup", "java.lang.Thread.State"]):
        print(line)

app_proc.terminate()
