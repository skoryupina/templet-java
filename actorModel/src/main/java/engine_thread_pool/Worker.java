package engine_thread_pool;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker extends Thread {
    private Logger LOG = LoggerFactory.getLogger(Worker.class);
    private Message message;

    public Worker(@NotNull Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        message.setSending(false);
        @NotNull Actor actor = message.getActor();
        actor.recv(message);
    }
}