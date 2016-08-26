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
package seq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

public class Engine {

    Logger LOG = LoggerFactory.getLogger(Engine.class);
    /**
     * Очередь сообщений, готовых к обработке акторами.
     */
    private final Queue<Message> ready = new LinkedList<>();

    Queue<Message> getReady() {
        return ready;
    }

    public void run() {
//        while (ready.size() != 0) {
        while (true) {
            {
                if (!ready.isEmpty()) {
                    synchronized (ready) {
                        LOG.debug("ready size {}", ready.size());
                        Message message = ready.poll();
                        LOG.debug("ready size after {}", ready.size());
                        message.sending = false;
                        //todo Доработать логику - из одного потока вызываем метод другого
                        message.actor.recv(message, message.actor);
                    }
                }
            }
        }
    }
}
