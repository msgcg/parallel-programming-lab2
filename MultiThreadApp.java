import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;

class ComputeRects extends Thread {
    private MultiThreadApp parent;
    private volatile boolean going = true;
    private Random random = new Random();

    public ComputeRects(MultiThreadApp parent, ThreadGroup group, String name) {
        super(group, name);
        this.parent = parent;
    }

    public void stopThread() {
        this.going = false;
    }

    @Override
    public void run() {
        while (going) {
            int w = Math.max(10, parent.getCanvasWidth() - 40);
            int h = Math.max(10, parent.getCanvasHeight() - 40);
            parent.rectCoordinates(random.nextInt(w), random.nextInt(h));
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

class ComputeOvals extends Thread {
    private MultiThreadApp parent;
    private volatile boolean going = true;
    private Random random = new Random();

    public ComputeOvals(MultiThreadApp parent, ThreadGroup group, String name) {
        super(group, name);
        this.parent = parent;
    }

    public void stopThread() {
        this.going = false;
    }

    @Override
    public void run() {
        while (going) {
            int w = Math.max(10, parent.getCanvasWidth() - 40);
            int h = Math.max(10, parent.getCanvasHeight() - 40);
            parent.ovalCoordinates(random.nextInt(w), random.nextInt(h));
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

class ComputeLines implements Runnable {
    private MultiThreadApp parent;
    private volatile boolean going = true;
    private Random random = new Random();

    public ComputeLines(MultiThreadApp parent) {
        this.parent = parent;
    }

    public void stopThread() {
        this.going = false;
    }

    public void compute() {
        int w = Math.max(10, parent.getCanvasWidth() - 10);
        int h = Math.max(10, parent.getCanvasHeight() - 10);
        parent.lineCoordinates(random.nextInt(w), random.nextInt(h), random.nextInt(w), random.nextInt(h));
    }

    @Override
    public void run() {
        while (going) {
            compute();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

public class MultiThreadApp extends JFrame {
    private int m_rectX = 60, m_rectY = 60;
    private int m_ovalX = 220, m_ovalY = 140;
    private int m_lineX1 = 40, m_lineY1 = 220, m_lineX2 = 320, m_lineY2 = 80;

    private ComputeRects m_rects = null;
    private ComputeOvals m_ovals = null;
    private ComputeLines m_linesRunnable = null;
    private Thread m_linesThread = null;
    private ThreadGroup m_customGroup = null;

    private JPanel canvasPanel;
    private JLabel statusLabel;

    public MultiThreadApp() {
        super("Лабораторная работа №2: Потоки и процессы в Java (Эрденко М.Д.)");
        setSize(780, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(new Color(235, 240, 248));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        JLabel titleLabel = new JLabel("Параллельное программирование: Многопоточная визуализация примитивов");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(new Color(20, 45, 90));

        statusLabel = new JLabel("Группа потоков: main | Активные потоки: 0 | Состояние: Инициализация");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        headerPanel.add(titleLabel);
        headerPanel.add(statusLabel);
        add(headerPanel, BorderLayout.NORTH);

        canvasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(250, 250, 252));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(230, 235, 240));
                for (int x = 0; x < getWidth(); x += 40) g2d.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 40) g2d.drawLine(0, y, getWidth(), y);

                g2d.setColor(new Color(40, 160, 60));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(m_lineX1, m_lineY1, m_lineX2, m_lineY2);
                g2d.drawString("ComputeLines (Runnable)", Math.min(m_lineX1, m_lineX2) + 5, Math.min(m_lineY1, m_lineY2) - 5);

                g2d.setColor(new Color(220, 50, 50));
                g2d.fillRect(m_rectX, m_rectY, 32, 32);
                g2d.setColor(new Color(150, 20, 20));
                g2d.drawRect(m_rectX, m_rectY, 32, 32);
                g2d.drawString("ComputeRects (Thread)", m_rectX, m_rectY - 6);

                g2d.setColor(new Color(50, 100, 220));
                g2d.fillOval(m_ovalX, m_ovalY, 34, 34);
                g2d.setColor(new Color(20, 50, 150));
                g2d.drawOval(m_ovalX, m_ovalY, 34, 34);
                g2d.drawString("ComputeOvals (Thread)", m_ovalX, m_ovalY - 6);
            }
        };
        canvasPanel.setPreferredSize(new Dimension(760, 380));
        add(canvasPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        controlPanel.setBackground(new Color(240, 242, 245));

        JButton btnStart = new JButton("Запустить потоки");
        JButton btnStop = new JButton("Остановить потоки");
        JButton btnBench = new JButton("Тест вычислений");

        btnStart.addActionListener(e -> startThreads());
        btnStop.addActionListener(e -> stopThreads());
        btnBench.addActionListener(e -> runBenchmark());

        controlPanel.add(btnStart);
        controlPanel.add(btnStop);
        controlPanel.add(btnBench);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public int getCanvasWidth() {
        return canvasPanel.getWidth() > 0 ? canvasPanel.getWidth() : 740;
    }

    public int getCanvasHeight() {
        return canvasPanel.getHeight() > 0 ? canvasPanel.getHeight() : 360;
    }

    public synchronized void rectCoordinates(int x, int y) {
        this.m_rectX = x;
        this.m_rectY = y;
        canvasPanel.repaint();
    }

    public synchronized void ovalCoordinates(int x, int y) {
        this.m_ovalX = x;
        this.m_ovalY = y;
        canvasPanel.repaint();
    }

    public synchronized void lineCoordinates(int x1, int y1, int x2, int y2) {
        this.m_lineX1 = x1;
        this.m_lineY1 = y1;
        this.m_lineX2 = x2;
        this.m_lineY2 = y2;
        canvasPanel.repaint();
    }

    public void startThreads() {
        if (m_rects == null || !m_rects.isAlive()) {
            m_customGroup = new ThreadGroup("RenderThreadGroup");

            m_rects = new ComputeRects(this, m_customGroup, "Thread-Rectangles");
            m_rects.setPriority(Thread.NORM_PRIORITY);
            m_rects.start();

            m_ovals = new ComputeOvals(this, m_customGroup, "Thread-Ovals");
            m_ovals.setPriority(Thread.NORM_PRIORITY + 2);
            m_ovals.start();

            m_linesRunnable = new ComputeLines(this);
            m_linesThread = new Thread(m_customGroup, m_linesRunnable, "Runnable-Lines");
            m_linesThread.setPriority(Thread.NORM_PRIORITY - 1);
            m_linesThread.start();

            updateStatus("Потоки активны | Группа: " + m_customGroup.getName() + " | Потоков в группе: " + m_customGroup.activeCount());
            System.out.println("[МНОГОПОТОЧНОСТЬ] Запущены 3 вычислительных потока:");
            System.out.println("  1. " + m_rects.getName() + " (Приоритет: " + m_rects.getPriority() + ")");
            System.out.println("  2. " + m_ovals.getName() + " (Приоритет: " + m_ovals.getPriority() + ")");
            System.out.println("  3. " + m_linesThread.getName() + " (Приоритет: " + m_linesThread.getPriority() + ")");
        }
    }

    public void stopThreads() {
        if (m_rects != null) m_rects.stopThread();
        if (m_ovals != null) m_ovals.stopThread();
        if (m_linesRunnable != null) m_linesRunnable.stopThread();
        updateStatus("Потоки остановлены управляющей переменной going = false");
        System.out.println("[МНОГОПОТОЧНОСТЬ] Потоки корректно остановлены.");
    }

    private void updateStatus(String msg) {
        statusLabel.setText(msg);
    }

    public void saveScreenshot(String path) {
        try {
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            paint(g2);
            g2.dispose();
            File out = new File(path);
            out.getParentFile().mkdirs();
            ImageIO.write(image, "png", out);
            System.out.println("Сохранен скриншот окна: " + path);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения скриншота: " + e.getMessage());
        }
    }

    public static void runBenchmark() {
        System.out.println("\n=== ТЕСТ ПРОИЗВОДИТЕЛЬНОСТИ: ПАРАЛЛЕЛЬНЫЕ ВЫЧИСЛЕНИЯ ===");
        int totalElements = 40_000_000;
        int[] threadCounts = {1, 2, 4, 8, 16};
        long singleThreadTime = 0;

        for (int threads : threadCounts) {
            System.out.printf("  [Тест] Вычисление в %2d поток(ов)... ", threads);
            long startTime = System.currentTimeMillis();
            Thread[] workerThreads = new Thread[threads];
            int chunkSize = totalElements / threads;

            for (int t = 0; t < threads; t++) {
                final int startIdx = t * chunkSize;
                final int endIdx = (t == threads - 1) ? totalElements : startIdx + chunkSize;
                workerThreads[t] = new Thread(() -> {
                    double sum = 0;
                    for (int i = startIdx; i < endIdx; i++) {
                        sum += Math.sin(i) * Math.cos(i);
                    }
                });
                workerThreads[t].start();
            }

            for (Thread t : workerThreads) {
                try {
                    t.join();
                } catch (InterruptedException ignored) {}
            }

            long elapsed = System.currentTimeMillis() - startTime;
            if (threads == 1) singleThreadTime = elapsed;
            double speedup = (double) singleThreadTime / Math.max(1, elapsed);
            System.out.printf("готово за %4d мс | Ускорение: %.2fx%n", elapsed, speedup);
        }
        System.out.println("=========================================================\n");
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--benchmark")) {
            runBenchmark();
            return;
        }

        if (args.length > 1 && args[0].equals("--capture")) {
            MultiThreadApp app = new MultiThreadApp();
            app.pack();
            app.setSize(780, 520);
            app.startThreads();
            try {
                Thread.sleep(600);
            } catch (InterruptedException ignored) {}
            app.saveScreenshot(args[1]);
            app.stopThreads();
            System.exit(0);
        }

        SwingUtilities.invokeLater(() -> {
            MultiThreadApp app = new MultiThreadApp();
            app.setVisible(true);
            app.startThreads();
        });
    }
}
