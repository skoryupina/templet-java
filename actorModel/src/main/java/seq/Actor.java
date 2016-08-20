package seq;

public abstract class Actor {
    public Engine engine;

    public abstract void recv(Message message, Actor actor);
}