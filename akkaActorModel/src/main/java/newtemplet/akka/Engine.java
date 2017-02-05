package newtemplet.akka;

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

}
