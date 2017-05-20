package thread_based_version;

import engine_msg_queue.Engine;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class ThreadBasedVersionTest {
    private static Logger LOG = LoggerFactory.getLogger(ThreadBasedVersionTest.class);

    /**
     * Значение для проверки тождества.
     */
    private static final int VALUE = 4;

    @Before
    public void beforeTests() {
        //region Actors initialization
        ThreadBasedVersion.master = new ThreadBasedVersion.Master();
        ThreadBasedVersion.workerSin = new ThreadBasedVersion.WorkerSin();
        ThreadBasedVersion.workerCos = new ThreadBasedVersion.WorkerCos();
        //endregion

        //region Messages initialization
        ThreadBasedVersion.messageSin = new ThreadBasedVersion.MessageSin();
        ThreadBasedVersion.messageCos = new ThreadBasedVersion.MessageCos();
        ThreadBasedVersion.engine = new Engine();

        ThreadBasedVersion.engine.addActor(ThreadBasedVersion.workerSin);
        ThreadBasedVersion.engine.addActor(ThreadBasedVersion.workerCos);
        ThreadBasedVersion.engine.addActor(ThreadBasedVersion.master);

        ThreadBasedVersion.messageSin.setX(VALUE);
        ThreadBasedVersion.messageCos.setX(VALUE);
        //endregion
    }

    @Test
    public void isPythagoreanTrigonometricIdentity() throws InterruptedException {

        ThreadBasedVersion.messageSin.send(ThreadBasedVersion.workerSin);
        ThreadBasedVersion.messageCos.send(ThreadBasedVersion.workerCos);
        Thread thread = new Thread(ThreadBasedVersion.engine);
        thread.start();
        thread.join();

        //Проверка тригонометрического тождества
        LOG.debug("Результат проверки тригонометрического тождества: " + ThreadBasedVersion.master.getResult());
        assertEquals(ThreadBasedVersion.master.getResult() - 1, 0);
    }
}
