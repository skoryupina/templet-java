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

package thread_pool;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import par.Actor;
import par.Engine;
import par.Message;

public class ThreadPoolVersion {
    private static Logger LOG = LoggerFactory.getLogger(ThreadPoolVersion.class);

    public static Master master;
    public static WorkerSin workerSin;
    public static WorkerCos workerCos;
    public static MessageSin messageSin;
    public static MessageCos messageCos;
    public static Engine engine;

    //region Message declaration
    public static class MessageSin extends Message {
        private double x;
        private double SinX2;

        double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getSinX2() {
            return SinX2;
        }

        void setSinX2(double sinX2) {
            SinX2 = sinX2;
        }
    }

    public static class MessageCos extends Message {
        private double x;
        private double CosX2;

        double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getCosX2() {
            return CosX2;
        }

        void setCosX2(double cosX2) {
            CosX2 = cosX2;
        }
    }
    //endregion


    public static class Master extends Actor {
        private double result;

        public Master(@NotNull Engine engine) {
            super(engine);
        }

        public double getResult() {
            return result;
        }

        public void recv(Message msg) {

            if (messageSin.access(this, messageSin) && messageCos.access(this, messageCos)) {
                result = messageSin.SinX2 + messageCos.CosX2;
                engine.setFinished(true);
                LOG.debug("Result of calculations:" + result);
            }
        }
    }

    public static class WorkerSin extends Actor {
        private static int POW = 2;

        public WorkerSin(@NotNull Engine engine) {
            super(engine);
        }

        @Override
        public void recv(Message msg) {
            if (msg instanceof MessageSin && msg.access(this, msg)) {
                MessageSin messageSin = (MessageSin) msg;
                double value = Math.pow(Math.sin(messageSin.getX()), POW);
                messageSin.setSinX2(value);
                msg.send(engine, master);
            }
        }
    }


    public static class WorkerCos extends Actor {
        private static int POW = 2;

        public WorkerCos(@NotNull Engine engine) {
            super(engine);
        }

        @Override
        public void recv(Message msg) {
            if (msg instanceof MessageCos && msg.access(this, msg)) {
                MessageCos messageCos = (MessageCos) msg;
                double value = Math.pow(Math.cos(messageCos.getX()), POW);
                messageCos.setCosX2(value);
                msg.send(engine, master);
            }
        }
    }

}
