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
package engine_thread_pool;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Engine {

    private AtomicBoolean finished = new AtomicBoolean(false);

    /**
     * Число потоков в пуле.
     */
    private static final int CAPACITY = 10;

    /**
     * Очередь сообщений, готовых к обработке акторами.
     */
    private final Queue<Message> ready = new LinkedList<>();

    private ExecutorService threadPool = Executors.newWorkStealingPool();

    Queue<Message> getReady() {
        return ready;
    }

    public Boolean isFinished() {
        return finished.get();
    }

    public void setFinished(Boolean finished) {
        this.finished.set(finished);
    }

    public void run() {
        while (!finished.get()) {
            synchronized (this) {
                while (ready.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message message = ready.poll();
                Runnable worker = new Worker(message);
                threadPool.execute(worker);
            }
        }
    }
}
