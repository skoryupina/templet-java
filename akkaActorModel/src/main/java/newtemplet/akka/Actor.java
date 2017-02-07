package newtemplet.akka;

import akka.actor.UntypedActor;
import newtemplet.Main;

import static newtemplet.Main.N;

public class Actor extends UntypedActor {

    private int id;
    private boolean access_ms_id_minus_1 = false;
    private boolean access_ms_id = false;


    public Actor(int id) {
        this.id = id;
    }

    @Override
    public final void onReceive(Object message) throws Exception {
        System.out.println("************** #id" + id);
        if (((Integer) message) == id - 1) access_ms_id_minus_1 = true;
        if (((Integer) message) == id) access_ms_id = true;
        System.out.println("#id" + id + "    id-1 " + access_ms_id_minus_1 + " access_ms_id" + access_ms_id);

        if ((id == 0 || access_ms_id_minus_1) &&
                (id == N - 1 || access_ms_id) &&
                (Main.time[id] <= Main.T)) {
            Main.operation(id + 1);
            Main.time[id]++;
            System.out.println("id#" + id + " time " + Main.time[id]);

            if (id != 0) {
                System.out.println("tell id# " + Main.actors[id - 1]);
                Main.actors[id - 1].tell(id - 1, getSelf());
                access_ms_id_minus_1 = false;
            }
            if (id != Main.N - 1) {
                System.out.println("tell id# " + Main.actors[id + 1]);
                Main.actors[id + 1].tell(id, getSelf());
                access_ms_id = false;
            }
        } else if (Main.time[id] > Main.T) {
            context().stop(getSelf());
            Main.active.getAndDecrement();
        }
    }
}