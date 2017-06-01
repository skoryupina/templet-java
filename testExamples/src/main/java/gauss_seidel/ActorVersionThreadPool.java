package gauss_seidel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import seq.Actor;
import seq.Engine;
import seq.Message;

public class ActorVersionThreadPool {
    static Logger LOG = LoggerFactory.getLogger(ActorVersionThreadPool.class);

    public static Actor[] a;
    public static ActorVersionThreadPool.MessageGauss[] m;
    //служит для отметки моментов времени
    public static int[] t;
    private static double[][] u;
    //todo:
    public static final int N = InputMatrixHelper.SIZE_Y - 2;


    public static double[][] calcWithActors(double[][] u) {
        Engine engine = new Engine();
        a = new ActorVersion.GaussSeidelActor[N];
//        LOG.debug("a size " + a.length);
        m = new ActorVersionThreadPool.MessageGauss[N - 1];
//        LOG.debug("m size " + m.length);
        t = new int[N];
//        LOG.debug("t size " + t.length);
        ActorVersionThreadPool.u = u;

        /***
         * Инициализация массива акторов
         */

        for (int i = 0; i < N; i++) {
            a[i] = new ActorVersionThreadPool.GaussSeidelActor();
            a[i].id = i;
            t[i] = 1;
            a[i].engine = engine;
            Thread thread = new Thread(a[i], String.valueOf(a[i].id));
            thread.start();
        }

        /***
         * Инициализация массива сообщений
         */
        for (int i = 0; i < N - 1; i++) {
            m[i] = new ActorVersionThreadPool.MessageGauss(i);
            m[i].id = i;
//            m[i].send(engine, m[i], a[i]); //отправляем сообщение i-му актору
            m[i].sending = (i == 0);
        }
        m[0].send(engine, m[0], a[0]); //отправляем сообщение 0-му актору

        engine.run();
        return ActorVersionThreadPool.u;
    }

    static class GaussSeidelActor extends Actor {
        private boolean access_ms_id_minus_1 = false;
        private boolean access_ms_id = true;
//
//             if (((Integer) message) == id - 1) access_ms_id_minus_1 = true;
//        if (((Integer) message) == id) access_ms_id = true;
//        if ((id == 0 || access_ms_id_minus_1) &&
//                (id == N - 1 || access_ms_id) &&
//                (Main.time[id] <= Main.T)) {
//            Main.operation(id + 1);
//            Main.time[id]++;
//
//            if (id != 0) {
//                Main.actors[id - 1].tell(id - 1, getSelf());
//                access_ms_id_minus_1 = false;
//            }
//            if (id != Main.N - 1) {
//                Main.actors[id + 1].tell(id, getSelf());
//                access_ms_id = false;
//            }
//        }
//        if (Main.time[id] == Main.T && id == Main.actors.length - 1) {
//            Main.system.terminate();
//        }


        @Override
        public void recv(Message message) {
            LOG.error("actor " + this.id);
            LOG.debug(Thread.currentThread().getName());
            ActorVersionThreadPool.MessageGauss msg = (ActorVersionThreadPool.MessageGauss) message;
            int i = msg.getX();
            if (i == id - 1) access_ms_id_minus_1 = true;
            if (i == id) access_ms_id = true;

            MDC.put("id", String.valueOf(id));
            LOG.debug("**check conditions***** for id: " + id + "  msg: " + msg.getX());
            LOG.debug("*1*" + (id == 0 || access_ms_id_minus_1));
            LOG.debug("*2*" + (id == N - 1 || access_ms_id));
            LOG.debug("*3*" + "t[" + id + "]=" + t[id] + "  " + (t[id] <= InputMatrixHelper.ITERATIONS));
            LOG.debug("************************");


            if ((id == 0 || access_ms_id_minus_1) &&
                    (id == N - 1 || access_ms_id) &&
                    (t[id] <= InputMatrixHelper.ITERATIONS)) {
                LOG.debug("****************************");

                operation(id + 1);
                t[id]++;

                if (id != 0) {
                    access_ms_id_minus_1 = false;
                    message.send(a[id - 1].engine, m[id - 1], a[id - 1]);
                    LOG.debug("1 send **id != 0** сообщение: " + m[id - 1].getX() + " актор № " + (id - 1));
//                    System.out.println("Send " + id);

                }
                if (id != N - 1) {
                    access_ms_id = false;
                    message.send(a[id + 1].engine, m[id], a[id + 1]);
                    LOG.debug("2 **id != N - 1** сообщение: " + (m[id].getX()) + " актор № " + (id + 1));
                    //System.out.println("Send " + id);

                }
            }
            if (t[id] == InputMatrixHelper.ITERATIONS && id == N - 1) {
                System.out.println("finished " + id);
                a[id].engine.setFinished(true);
            }


//            int i = msg.getX();
//            LOG.debug("i== {}", i);
//            if ((i == 0 ||
//                    message.access(m[i - 1], a[i])) &&
//                            (i == N - 1 || message.access(m[i], a[i])) &&
//                    (t[i] <= InputMatrixHelper.ITERATIONS)) {
//                operation(i + 1);
//                t[i]++;
//                if (i != 0) {
//                    LOG.debug(Thread.currentThread().getName() + " vetv i != 0 ");
//                    message.send(a[i - 1].engine, m[i - 1], a[i - 1]);
//                }
//                if (i != N - 1) {
//                    LOG.debug(Thread.currentThread().getName() + " vetv i != N - 1");
//                    ((MessageGauss) message).from = "i != N - 1";
//                    message.send(a[i + 1].engine, m[i + 1], a[i + 1]);
//                    LOG.debug(((MessageGauss) message).from);
//                }
//            }
        }

    }

    private static class MessageGauss extends Message {
        int x;

        MessageGauss(int i) {
            x = i;
        }

        String from;

        int getX() {
            return x;
        }
    }

    private static void operation(int i) {
        for (int j = 1; j < InputMatrixHelper.SIZE_Y - 1; j++) {
            u[i][j] = (u[i][j - 1] + u[i][j + 1] + u[i - 1][j] + u[i + 1][j]) * 0.25;
        }
    }

}
