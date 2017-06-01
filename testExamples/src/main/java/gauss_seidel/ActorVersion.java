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
import org.slf4j.MDC;
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


    public static double[][] calcWithActors(double[][] u) {
        Engine engine = new Engine();
        a = new GaussSeidelActor[N];
        m = new MessageGauss[N - 1];
        t = new int[N];
        ActorVersion.u = u;

        /***
         * Инициализация массива акторов
         */

        for (int i = 0; i < N; i++) {
            a[i] = new GaussSeidelActor();
            a[i].id = i;
            t[i] = 1;
            a[i].engine = engine;
//            Thread thread = new Thread(a[i], String.valueOf(a[i].id));
//            thread.start();
        }

        /***
         * Инициализация массива сообщений
         */
        for (int i = 0; i < N - 1; i++) {
            m[i] = new MessageGauss(i);
            m[i].id = i;
            m[i].sending = (i == 0);
        }
        m[0].send(engine, m[0], a[0]); //отправляем сообщение 0-му актору

        engine.run();
        return ActorVersion.u;
    }

    static class GaussSeidelActor extends Actor {
        private boolean access_ms_id_minus_1 = false;
        private boolean access_ms_id = true;

        @Override
        public void recv(Message message) {
            LOG.error("actor " + this.id);
            LOG.debug(Thread.currentThread().getName());
            MessageGauss msg = (MessageGauss) message;
            int i = msg.getX();
            if (i == id - 1) access_ms_id_minus_1 = true;
            if (i == id) access_ms_id = true;

            MDC.put("id", String.valueOf(id));
            LOG.debug("**check conditions***** for id: " + id + "  msg: " + msg.getX());
            LOG.debug("*1*" + (id == 0 || access_ms_id_minus_1));
            LOG.debug("*2*" + (id == N - 1 || access_ms_id));
            LOG.debug("*3*" + (t[id] <= InputMatrixHelper.ITERATIONS));
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
                    LOG.debug("1 send " + (id - 1));

                }
                if (id != N - 1) {
                    access_ms_id = false;
                    message.send(a[id + 1].engine, m[id], a[id + 1]);
                    LOG.debug("2 send " + (id + 1));

                }
            }
            if (t[id] == InputMatrixHelper.ITERATIONS && id == N - 1) {
                System.out.println("finished " + id);
                a[id].engine.setFinished(true);
            }
        }
    }

    private static class MessageGauss extends Message {
        int x;

        public MessageGauss(int i) {
            x = i;
        }

        public int getX() {
            return x;
        }
    }

    private static void operation(int i) {
        for (int j = 1; j < InputMatrixHelper.SIZE_Y - 1; j++) {
            u[i][j] = (u[i][j - 1] + u[i][j + 1] + u[i - 1][j] + u[i + 1][j]) * 0.25;
        }
    }
}
