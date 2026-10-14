public class OneClickLab2 {
    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("  ЛАБОРАТОРНАЯ РАБОТА №2: ПОТОКИ И ПРОЦЕССЫ В JAVA");
        System.out.println("  Выполнил: Эрденко М. Д. (ПО-32б) | Проверил: Степаков В. И.");
        System.out.println("==================================================================\n");

        System.out.println("[1/2] Запуск теста параллельных вычислений (1, 2, 4, 8 потоков)...");
        MultiThreadApp.runBenchmark();

        System.out.println("[2/2] Открытие графического окна Swing...");
        System.out.println("Окно открыто. Для завершения закройте окно программы.");
        MultiThreadApp.main(new String[0]);
    }
}
