package seq;

import java.util.LinkedList;
import java.util.Queue;

public class Engine {

    /**
     * Очередь сообщений, готовых к обработке акторами.
     */
    private final Queue<Message> ready = new LinkedList<>();

    Queue<Message> getReady() {
        return ready;
    }

    public void run() {
//        while (ready.size() != 0) {
        while (true) {
            {
                if (!ready.isEmpty()) {
                    synchronized (ready) {
                        Message message = ready.poll();
                        message.sending = false;
                        message.actor.recv(message, message.actor);
                    }
                }
            }
        }
    }
}
