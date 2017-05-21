/*  Copyright 2015 Sergey Vostokin, Ekaterina Skoryupina                    */
/*                                                                          */
/*  Licensed under the Apache License, Version 2.0 (the "License");         */
/*  you may not use this file except in compliance with the License.        */
/*  You may obtain a copy of the License at                                 */
/*                                                                          */
/*  http://www.apache.org/licenses/LICENSE-2.0                              */
/*                                                                          */
/*  Unless required by applicable law or agreed to in writing, software     */
/*  distributed under the License is distributed on an "AS IS" BASIS,       */
/*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*/
/*  See the License for the specific language governing permissions and     */
/*  limitations under the License.                                          */
/*--------------------------------------------------------------------------*/
package thread_pool;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import par.Engine;

import static org.junit.Assert.assertEquals;

/**
 * Проверяет тригонометрическое тождество на версии с потоковым пулом.
 */
public class ThreadPoolVersionTest {

    private static Logger LOG = LoggerFactory.getLogger(ThreadPoolVersionTest.class);

    /**
     * Значение для проверки тождества.
     */
    private static final int VALUE = 4;

    @Before
    public void beforeTests() {
        ThreadPoolVersion.engine = new Engine();

        //region Actors initialization
        ThreadPoolVersion.master = new ThreadPoolVersion.Master(ThreadPoolVersion.engine);
        ThreadPoolVersion.workerSin = new ThreadPoolVersion.WorkerSin(ThreadPoolVersion.engine);
        ThreadPoolVersion.workerCos = new ThreadPoolVersion.WorkerCos(ThreadPoolVersion.engine);
        //endregion

        //region Messages initialization
        ThreadPoolVersion.messageSin = new ThreadPoolVersion.MessageSin();
        ThreadPoolVersion.messageCos = new ThreadPoolVersion.MessageCos();

        ThreadPoolVersion.messageSin.setX(VALUE);
        ThreadPoolVersion.messageCos.setX(VALUE);
        //endregion
    }

    @Test
    public void isPythagoreanTrigonometricIdentity() throws InterruptedException {
        final double DELTA = 1e-15;

        ThreadPoolVersion.messageSin.send(ThreadPoolVersion.engine, ThreadPoolVersion.workerSin);
        ThreadPoolVersion.messageCos.send(ThreadPoolVersion.engine, ThreadPoolVersion.workerCos);

        Thread thread = new Thread(ThreadPoolVersion.engine);
        thread.start();
        thread.join();

        //Проверка тригонометрического тождества
        LOG.debug("Результат проверки тригонометрического тождества: " + ThreadPoolVersion.master.getResult());


        //Проверка тригонометрического тождества
        assertEquals(ThreadPoolVersion.master.getResult(), 1, DELTA);
    }
}
