package seq;

public class Message {
    public Actor actor;
    public boolean sending;


    public void send(Engine engine, Message message, Actor actor) {
        if (message.sending) {
            return;
        }
        message.sending = true;
        message.actor = actor;
        engine.ready.add(message);
    }

    public boolean access(Message message, Actor actor) {
        return message.actor == actor && !message.sending;
    }
}
