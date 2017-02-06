package newtemplet.akka;

import akka.actor.ActorRef;

import java.io.Serializable;

/**
 * Базовая реализация сообщения.
 */
public class MessageBase implements Serializable {

    private ActorRef actor;

    private boolean sending;

    public ActorRef getActor() {
        return actor;
    }

    public void setActor(ActorRef actor) {
        this.actor = actor;
    }

    public boolean isSending() {
        return sending;
    }

    public void setSending(boolean sending) {
        this.sending = sending;
    }

    public MessageBase() {

    }

    public static void send(Engine engine, MessageBase message, ActorRef actor) {
        if (message.isSending())
            return;
        message.setSending(true);
        message.setActor(actor);
        engine.getReady().add(message);
    }

    public static boolean access(MessageBase message, ActorRef actor) {
        return message.getActor() != null && actor != null && message.getActor().equals(actor) && !message.isSending();
    }
}
