package newtemplet.akka;

import akka.actor.ActorRef;

import java.util.LinkedList;
import java.util.Queue;

public class Engine {

    /**
     * Число потоков, активных в определенный момент времени.
     */
    private volatile int active;

    /**
     * Очередь сообщений, готовых к обработке акторами.
     */
    private final Queue<MessageBase> ready = new LinkedList<>();

    public Queue<MessageBase> getReady() {
        return ready;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public boolean isFinished = false;

    public double run() {
        double time = System.currentTimeMillis();
        while (!isFinished) {
            if (ready.size() != 0) {
                synchronized (ready) {
                    MessageBase message = ready.poll();
                    message.setSending(false);
                    ActorRef self = message.getActor();
                    self.tell(message, self);
                }
            }
        }
        return System.currentTimeMillis() - time;
    }

}
