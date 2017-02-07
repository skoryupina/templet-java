package newtemplet.akka;

import akka.actor.ActorRef;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Engine {

    /**
     * Число потоков, активных в определенный момент времени.
     */
    private AtomicInteger active;

    /**
     * Очередь сообщений, готовых к обработке акторами.
     */
    private final Queue<MessageBase> ready = new LinkedList<>();

    public Queue<MessageBase> getReady() {
        return ready;
    }

    public int getActive() {
        return active.get();
    }

    public void decreaseActive() {
        active.getAndDecrement();
    }

    public void setActive(int n) {
        active.set(n);
    }

    public double run() {
        double time = System.currentTimeMillis();
//        System.out.println(isFinished + " finished");
        while (active.get() != 0) {
//            System.out.println(ready.size() + " size");
            if (ready.size() != 0) {
                synchronized (ready) {
                    MessageBase message = ready.poll();
                    message.setSending(false);
                    ActorRef self = message.getActor();
                    self.tell(message, self);
                }
            }
//            System.out.println(ready.size() + " size");
        }
        return System.currentTimeMillis() - time;
    }

    public void add(MessageBase message) {
        synchronized (ready) {
            ready.add(message);
        }
    }

}
