package gauss_seidel;

import engine_thread_pool.Engine;

import java.util.Random;


public class Main {

    private static final int SCALE = 10;

    private static final int W = SCALE * 2;
    private static final int H = SCALE;
    public static final int T = 10 * SCALE * 2;
    public static final int N = H - 2;

    private static double[][] field = new double[H][W];
    private static double[][] field1 = new double[H][W];

    private static final int OBS_N = 3;

    private static double obs_seq[] = new double[OBS_N];
    private static double seq_max, seq_mid, seq_min;

    private static double obs_thread_pool[] = new double[OBS_N];
    private static double thread_pool_max, thread_poll_mid, thread_pool_min;

    //seq version
//    public static Actor[] a;
//    public static ActorVersion.MessageGauss[] m;
//    public static int time[] = new int[N];

    //thread poll version
    public static engine_thread_pool.Actor[] a;
    public static ActorVersion_ThreadPoolVersion.MessageGauss[] m;
    public static int time[] = new int[N];


    public static void shufle() {
        Random random = new Random();
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                double srand = random.nextDouble();
                field[i][j] = srand;
                field1[i][j] = srand;
            }
        }
    }

    private static double seq_alg() {
        long time = System.nanoTime();

        for (int t = 1; t <= T; t++)
            for (int i = 1; i < H - 1; i++) operation1(i);
        return ((double) System.nanoTime() - time) / 1E9;
    }

    private static boolean compare() {
        for (int i = 0; i < H; i++)
            for (int j = 0; j < W; j++)
                if (field1[i][j] != field[i][j]) {
                    System.out.println("\nWrong: i=" + i + ";j=" + j + "  seq=" + field1[i][j] + "  threadpool=" + field[i][j] + "\n");
                    return false;
                }
        return true;
    }

    private static void min_max() {
        seq_max = seq_min = obs_seq[0];
        thread_pool_max = thread_pool_min = obs_thread_pool[0];

        for (int i = 1; i < OBS_N; i++) {
            seq_max = obs_seq[i] > seq_max ? obs_seq[i] : seq_max;
            seq_min = obs_seq[i] < seq_min ? obs_seq[i] : seq_min;

            thread_pool_max = obs_thread_pool[i] > thread_pool_max ? obs_thread_pool[i] : thread_pool_max;
            thread_pool_min = obs_thread_pool[i] < thread_pool_min ? obs_thread_pool[i] : thread_pool_min;
        }
        seq_mid = (seq_min + seq_max) / 2;
        thread_poll_mid = (thread_pool_min + thread_pool_max) / 2;
    }

    public static void operation1(int i) {
        for (int j = 1; j < W - 1; j++) {
            field1[i][j] = (field1[i][j - 1] + field1[i][j + 1] + field1[i - 1][j] + field1[i + 1][j]) * 0.25;
        }
    }

    public static void operation(int i) {
        for (int j = 1; j < W - 1; j++) {
            field[i][j] = (field[i][j - 1] + field[i][j + 1] + field[i - 1][j] + field[i + 1][j]) * 0.25;
        }
    }

    static double par_thread_pool() {
        Engine engine = new Engine();
        a = new engine_thread_pool.Actor[N];
        m = new ActorVersion_ThreadPoolVersion.MessageGauss[N - 1];

        for (int i = 0; i < N; i++) {
            a[i] = new ActorVersion_ThreadPoolVersion.GaussSeidelActor_ThreadPool();
            a[i].id = i;
            time[i] = 1;
            a[i].engine = engine;
        }

        /***
         * Инициализация массива сообщений
         */
        for (int i = 0; i < N - 1; i++) {
            m[i] = new ActorVersion_ThreadPoolVersion.MessageGauss(i);
            m[i].id = i;
            m[i].sending = (i == 0);
        }

        long time = System.nanoTime();
        m[0].send(engine, m[0], a[0]); //отправляем сообщение 0-му актору
        engine.run();
        return ((double) System.nanoTime() - time) / 1E9;
    }

    public static void main(String[] args) {
        for (int i = 0; i < OBS_N; i++) {
            shufle();

            //obs_seq[i] = seq_alg();
            obs_thread_pool[i] = par_thread_pool();
            System.out.println(compare() ? "Thread pool Ok" : " something wrong in thread pool");
            System.out.println((int) ((float) (i + 1) / OBS_N * 100) + "% done");
        }

        min_max();

        System.out.println("\nTest results for H = " + H + "; W = " + W + "; T = " + T);
        System.out.println("\nseq_min  = " + seq_min + " sec; " + "\nseq_mid  = " + seq_mid + " sec; \nseq_max  = " + seq_max + " sec\n");
        System.out.println("\nthread_pool_min  = " + thread_pool_min + " sec; " + "\nthread_poll_mid  = " + thread_poll_mid + " sec; \nthread_pool_max  = " + thread_pool_max + " sec\n");
    }


}