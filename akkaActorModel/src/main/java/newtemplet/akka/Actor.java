package newtemplet.akka;

import akka.actor.UntypedActor;
import newtemplet.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static newtemplet.Main.N;

public class Actor extends UntypedActor {
    static Logger LOG = LoggerFactory.getLogger(Actor.class);
    protected int id;
    private boolean access_ms_id_minus_1 = false;
    private boolean access_ms_id = true;

    public Actor() {

    }

    public Actor(int id) {
        this.id = id;
    }

    @Override
    public void onReceive(Object message) {
        if (((Integer) message) == id - 1) access_ms_id_minus_1 = true;
        if (((Integer) message) == id) access_ms_id = true;


        MDC.put("id", String.valueOf(id));
        LOG.debug("**check conditions***** for id: " + id + "  msg: " + (Integer) message);
        LOG.debug("*1*" + (id == 0 || access_ms_id_minus_1));
        LOG.debug("*2*" + (id == N - 1 || access_ms_id));
        LOG.debug("*3*" + "t[" + id + "]=" + Main.time[id] + "  " + (Main.time[id] <= Main.T));
        LOG.debug("************************");


        if ((id == 0 || access_ms_id_minus_1) &&
                (id == N - 1 || access_ms_id) &&
                (Main.time[id] <= Main.T)) {
            Main.operation(id + 1);
            Main.time[id]++;

            if (id != 0) {
                MDC.put("id", String.valueOf(id));
                LOG.debug("1 send **id != 0** " + " сообщение" + (id - 1) + " актор № " + (id - 1));
                Main.actors[id - 1].tell(id - 1, getSelf());
                access_ms_id_minus_1 = false;
            }
            if (id != Main.N - 1) {
                MDC.put("id", String.valueOf(id));
                LOG.debug("2 send **id != Main.N - 1** " + " сообщение" + (id) + " актор № " + (id + 1));
                Main.actors[id + 1].tell(id, getSelf());
                access_ms_id = false;
            }
        }
        if (Main.time[id] == Main.T && id == Main.actors.length - 1) {
            System.out.println("finished " + id);
            Main.system.terminate();
        }
    }
}