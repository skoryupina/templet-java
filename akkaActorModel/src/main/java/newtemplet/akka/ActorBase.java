package newtemplet.akka;

import akka.actor.UntypedActor;
import newtemplet.Main;

import static newtemplet.Main.N;

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
        MessageBase.access(message, this);

        if ((id == 0 ||
                MessageBase.access(Main.messages[id - 1], this)) &&
                (id == N - 1 || MessageBase.access(Main.messages[id], this)) &&
                (Main.time[id] <= Main.T)) {
            Main.operation(id + 1);
            Main.time[id]++;

            if (id != 0) {
                MessageBase.send(Main.engine, Main.messages[id - 1], Main.actors[id - 1]);
            }
            if (id != Main.N - 1) {
                MessageBase.send(Main.engine, Main.messages[id], Main.actors[id + 1]);
            }
        }
    }
}