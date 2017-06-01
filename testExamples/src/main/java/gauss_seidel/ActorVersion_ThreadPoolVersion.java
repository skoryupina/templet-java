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

import engine_thread_pool.Actor;
import engine_thread_pool.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static gauss_seidel.Main.*;

public class ActorVersion_ThreadPoolVersion {
    static Logger LOG = LoggerFactory.getLogger(ActorVersion_ThreadPoolVersion.class);

    public static class GaussSeidelActor_ThreadPool extends Actor {
        private boolean access_ms_id_minus_1 = false;
        private boolean access_ms_id = true;

        @Override
        public void recv(Message message) {
            MessageGauss msg = (MessageGauss) message;
            int i = msg.getX();
            if (i == id - 1) access_ms_id_minus_1 = true;
            if (i == id) access_ms_id = true;

            if ((id == 0 || access_ms_id_minus_1) &&
                    (id == N - 1 || access_ms_id) &&
                    (Main.time[id] <= Main.T)) {
                Main.operation(id + 1);
                Main.time[id]++;
                System.out.println(id + "  " + String.valueOf(Main.time[id]));

                if (id != 0) {
                    access_ms_id_minus_1 = false;
                    message.send(a[id - 1].engine, m[id - 1], a[id - 1]);
                    LOG.debug("1 send **id != 0** сообщение: " + m[id - 1].getX() + " актор № " + (id - 1));

                }
                if (id != N - 1) {
                    access_ms_id = false;
                    message.send(a[id + 1].engine, m[id], a[id + 1]);
                    LOG.debug("2 **id != N - 1** сообщение: " + (m[id].getX()) + " актор № " + (id + 1));

                }
            }
            if (Main.time[id] == Main.T && id == a.length - 1) {
                System.out.println("finished " + id);
                a[id].engine.setFinished(true);
            }
        }

    }

    public static class MessageGauss extends Message {
        int x;

        MessageGauss(int i) {
            x = i;
        }

        int getX() {
            return x;
        }
    }
}
