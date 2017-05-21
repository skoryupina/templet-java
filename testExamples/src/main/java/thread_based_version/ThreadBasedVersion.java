package thread_based_version;


import engine_msg_queue.Actor;
import engine_msg_queue.Engine;
import engine_msg_queue.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadBasedVersion {
    private static Logger LOG = LoggerFactory.getLogger(ThreadBasedVersion.class);

    public static ThreadBasedVersion.Master master;
    public static ThreadBasedVersion.WorkerSin workerSin;
    public static ThreadBasedVersion.WorkerCos workerCos;
    public static ThreadBasedVersion.MessageSin messageSin;
    public static ThreadBasedVersion.MessageCos messageCos;
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
        private int result;

        public int getResult() {
            return result;
        }

        public void recv(Message msg) {

            if (messageCos.access(this, messageCos) && messageSin.access(this, messageSin)) {
                result = (int) (messageSin.SinX2 + messageCos.CosX2);
                finished();
            }
        }
    }

    public static class WorkerSin extends Actor {
        private static int POW = 2;

        @Override
        public void recv(Message msg) {
            if (msg instanceof ThreadBasedVersion.MessageSin && msg.access(this, msg)) {
                ThreadBasedVersion.MessageSin messageSin = (ThreadBasedVersion.MessageSin) msg;
                double value = Math.pow(Math.sin(messageSin.getX()), POW);
                messageSin.setSinX2(value);
                msg.send(master);
            }
        }
    }


    public static class WorkerCos extends Actor {
        private static int POW = 2;

        @Override
        public void recv(Message msg) {
            if (msg instanceof ThreadBasedVersion.MessageCos && msg.access(this, msg)) {
                ThreadBasedVersion.MessageCos messageCos = (ThreadBasedVersion.MessageCos) msg;
                double value = Math.pow(Math.cos(messageCos.getX()), POW);
                messageCos.setCosX2(value);
                msg.send(master);
            }
        }
    }
}
