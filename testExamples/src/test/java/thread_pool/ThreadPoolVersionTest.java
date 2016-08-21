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
import par.Engine;

import static org.junit.Assert.assertEquals;

/**
 * Проверяет тригонометрическое тождество на версии с потоковым пулом.
 */
public class ThreadPoolVersionTest {

    /**
     * Значение для проверки тождества.
     */
    private static final int VALUE = 4;

    @Before
    public void beforeTests() {
    }

    @Test
    public void isPythagoreanTrigonometricIdentity() {
        final double DELTA = 1e-15;

        //region Actors initialization
        ThreadPoolVersion.master = new ThreadPoolVersion.Master();
        ThreadPoolVersion.workerSin = new ThreadPoolVersion.WorkerSin();
        ThreadPoolVersion.workerCos = new ThreadPoolVersion.WorkerCos();
        //endregion

        //region Messages initialization
        ThreadPoolVersion.messageSin = new ThreadPoolVersion.MessageSin();
        ThreadPoolVersion.messageCos = new ThreadPoolVersion.MessageCos();
        ThreadPoolVersion.engine = new Engine();

        ThreadPoolVersion.messageSin.setX(VALUE);
        ThreadPoolVersion.messageCos.setX(VALUE);
        //endregion


        ThreadPoolVersion.messageSin.send(ThreadPoolVersion.engine, ThreadPoolVersion.workerSin);
        ThreadPoolVersion.messageCos.send(ThreadPoolVersion.engine, ThreadPoolVersion.workerCos);
        ThreadPoolVersion.engine.start();

        //Проверка тригонометрического тождества
        assertEquals(ThreadPoolVersion.master.getResult(), 1, DELTA);
    }
}
