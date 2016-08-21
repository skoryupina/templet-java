package seq;

/**
 * Средство передачи знаний между акторами.
 */
public class Message {
    /**
     * Актор-получатель сообщения.
     */
    public Actor actor;

    /**
     * Статус сообщения.
     */
    public boolean sending;

    /**
     * Передать сообщение в очередь сообщений.
     *
     * @param engine Движок - среда обитания акторов.
     * @param actor  Получатель сообщения.
     */
    public void send(Engine engine, Message message, Actor actor) {
        if (message.sending) {
            return;
        }
        message.sending = true;
        message.actor = actor;
        synchronized (engine.getReady()) {
            engine.getReady().add(message);
        }
    }

    /**
     * Проверка доступа актора к сообщению.
     * Актор должен совпадать с получателем,
     * сообщение должно быть доставленным
     *
     * @param actor   Проверяемый актор.
     * @param message Проверяемое сообщение.
     * @return Есть ли право доступа.
     */
    public boolean access(Message message, Actor actor) {
        return message.actor == actor && !message.sending;
    }
}
