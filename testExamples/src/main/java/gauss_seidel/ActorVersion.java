/*  Copyright 2015 Sergey Vostokin, Ekaterina Skoryupina                    */
/*                                                                          */
/*  Licensed under the Apache License, Version 2.0 (the "License");         */
/*  you may not use this file except in compliance with the License.        */
/*  You may obtain a copy of the License at                                 */
/*                                                                          */
/*  http://www.apache.org/licenses/LICENSE-2.0                              */
/*                                                                          */
/*  Unless required by applicable law or agreed to in writing, software     */
/*  distributed under the License is distributed on an "AS IS" BASIS,       */
/*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*/
/*  See the License for the specific language governing permissions and     */
/*  limitations under the License.                                          */
/*--------------------------------------------------------------------------*/
package gauss_seidel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seq.Actor;
import seq.Engine;
import seq.Message;

public class ActorVersion {
    static Logger LOG = LoggerFactory.getLogger(ActorVersion.class);

    public static Actor[] a;
    public static MessageGauss[] m;
    //служит для отметки моментов времени
    public static int[] t;
    private static double[][] u;
    //todo:
    public static final int N = InputMatrixHelper.SIZE_Y - 2;


    public static void calcWithActors(double[][] u) {
        Engine engine = new Engine();
        a = new GaussSeidelActor[N];
//        LOG.debug("a size " + a.length);
        m = new MessageGauss[N - 1];
//        LOG.debug("m size " + m.length);
        t = new int[N];
//        LOG.debug("t size " + t.length);
        ActorVersion.u = u;

        /***
         * Инициализация массива акторов
         */

        for (int i = 0; i < N; i++) {
            a[i] = new GaussSeidelActor();
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
            m[i] = new MessageGauss(i);
            m[i].id = i;
            m[i].send(engine, m[i], a[i]); //отправляем сообщение i-му актору
            m[i].sending = (i == 0);
        }

        engine.run();
    }

    static class GaussSeidelActor extends Actor {

        @Override
        public void recv(Message message, Actor actor) {
            LOG.error("actor " + actor.id);
            LOG.debug(Thread.currentThread().getName());
            MessageGauss msg = (MessageGauss) message;
            int i = msg.getX();
            LOG.debug("i== {}", i);
            if ((i == 0 ||
                    message.access(m[i - 1], a[i])) &&
                            (i == N - 1 || message.access(m[i], a[i])) &&
                    (t[i] <= InputMatrixHelper.ITERATIONS)) {
                operation(i + 1);
                t[i]++;
                if (i != 0) {
                    LOG.debug(Thread.currentThread().getName() + " vetv i != 0 ");
                    message.send(a[i - 1].engine, m[i - 1], a[i - 1]);
                }
                if (i != N - 1) {
                    LOG.debug(Thread.currentThread().getName() + " vetv i != N - 1");
                    ((MessageGauss) message).from = "i != N - 1";
                    message.send(a[i + 1].engine, m[i + 1], a[i + 1]);
                    LOG.debug(((MessageGauss) message).from);
                }
            }
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
