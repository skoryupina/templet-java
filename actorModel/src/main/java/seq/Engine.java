package seq;

import java.util.LinkedList;
import java.util.Queue;

public class Engine {
    public Queue<Message> ready = new LinkedList<>();
    ;

    public void run() {
        while (ready.size() != 0) {
            System.out.println("Queue size" + ready.size());
            Message c = ready.poll();
            c.sending = false;
            c.actor.recv(c, c.actor);
        }
        System.out.println("Finished" + ready.size());
    }
}
