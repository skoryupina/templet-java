package gauss_seidel;

import seq.Actor;
import seq.Engine;
import seq.Message;

import java.util.Random;

public class ActorVersion {
    public static Actor[] a;
    public static MessageGauss[] m;
    public static final int ITERATIONS = 10000;
    //todo: t служит для отметки моментов времени
    public static int[] t;
    private static double[][] uAct;
    public static final int SIZE_X = 800;
    public static final int SIZE_Y = 80;
    //todo:
    public static final int N = SIZE_Y - 2;

    /***
     * Инициализация массива
     */
    public static void init() {
        uAct = new double[SIZE_X][SIZE_Y];
        Random random = new Random();
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                uAct[i][j] = random.nextDouble();
            }
        }
    }

    public static void calcWithActors() {
        init();
        Engine engine = new Engine();
        a = new AgentGaussSeidel[N];
        m = new MessageGauss[N - 1];
        t = new int[N];

        /***
         * Инициализация массива акторов
         */
        for (int i = 0; i < N; i++) {
            a[i] = new AgentGaussSeidel();
            a[i].engine = engine;
            //todo: int t[N]={1}
            t[i] = 1;
        }

        /***
         * Инициализация массива сообщений
         */
        for (int i = 0; i < N - 1; i++) {
            m[i] = new MessageGauss();
            m[i].send(engine, m[i], a[i]); //отправляем сообщение i-му актору
            m[i].sending = (i == 0);
        }
        a[0].engine.run();
    }

    static class AgentGaussSeidel extends Actor {

        @Override
        public void recv(Message message, Actor actor) {
            MessageGauss msg = (MessageGauss) message;
            int i = msg.getX();
            if ((i == 0 ||
                    message.access(m[i - 1], a[i]) &&
                            (i == N - 1 || message.access(m[i], a[i])) &&
                            (t[i] <= ITERATIONS))) {
                operation(i + 1, uAct);
                t[i]++;
                if (i != 0) {
                    message.send(a[i - 1].engine, m[i - 1], a[i - 1]);
                }
                if (i != N - 1) {
                    message.send(a[i + 1].engine, m[i], a[i + 1]);
                }
            }
        }

    }

    static class MessageGauss extends Message {
        int x;

        public int getX() {
            return x;
        }
    }

    public static void operation(int i, double[][] u) {
        for (int j = 1; j < SIZE_Y - 1; j++)
            u[i][j] = (u[i][j - 1] + u[i][j + 1] + u[i - 1][j] + u[i + 1][j]) * 0.25;
    }

}
