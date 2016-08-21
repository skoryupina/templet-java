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

package par;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Среда исполнения акторов.
 * Хранит пул потоков.
 */
public class Engine {

    /**
     * Число потоков в пуле.
     */
    private static final int CAPACITY = 2;

    /**
     * Число потоков, активных в определенный момент времени.
     */
    private volatile int active;

    /**
     * Очередь сообщений, готовых к обработке акторами.
     */
    private final Queue<Message> ready = new LinkedList<>();

    private ExecutorService threadPool = Executors.newFixedThreadPool(CAPACITY);

    Queue<Message> getReady() {
        return ready;
    }

    public void start() {
        while (true) {
            {
                if (!ready.isEmpty()) {
                    synchronized (ready) {
                        Message message = ready.poll();
                        Runnable worker = new Worker(message);
                        threadPool.execute(worker);
                    }
                }
            }
        }
    }
}
