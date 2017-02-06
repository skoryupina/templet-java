package newtemplet.akka;

import akka.actor.UntypedActor;
import newtemplet.Main;

import static newtemplet.Main.N;
import static newtemplet.Main.engine;

public class ActorBase extends UntypedActor {

    protected int id;

    public ActorBase(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public final void onReceive(Object o) throws Exception {
        MessageBase message = (MessageBase) o;
        MessageBase.access(message, getSelf());

        if ((id == 0 ||
                MessageBase.access(Main.messages[id - 1], getSelf())) &&
                (id == N - 1 || MessageBase.access(Main.messages[id], getSelf())) &&
                (Main.time[id] <= Main.T)) {
            Main.operation(id + 1);
            Main.time[id]++;

            if (id != 0) {
                MessageBase.send(Main.engine, Main.messages[id - 1], Main.actors[id - 1]);
            }
            if (id != Main.N - 1) {
                MessageBase.send(Main.engine, Main.messages[id], Main.actors[id + 1]);
            }
        } else {
            engine.isFinished = true;
        }
    }
}