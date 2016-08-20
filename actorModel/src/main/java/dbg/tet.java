/*--------------------------------------------------------------------------*/
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
package dbg;

import java.util.LinkedList;
import java.util.Queue;

public class tet {
    class Engine {
        Queue<Message> ready = new LinkedList<Message>();
    }

    abstract class Actor {
        abstract void recv(Message message, Actor actor);
    }

    class Message {
        Actor actor;
        boolean sending;

        public void send(Engine engine, Message message, Actor actor) {
            if (message.sending) {
                return;
            }
            message.sending = true;
            message.actor = actor;
            engine.ready.add(message);
        }

        public boolean access(Message message, Actor actor) {
            return message.actor == actor && !message.sending;
        }
    }


    public void run(Engine engine) {
        final int RAND_MAX = 32767;
        long rsize;
        rsize = engine.ready.size();
        while (rsize > 0) {
            int n = (int) ((Math.random() * RAND_MAX) % rsize);
            //TODO поменять структуру, для взятия случайного элемента из очереди
            Message message = (Message) engine.ready.poll();
            message.sending = false;
            message.actor.recv(message, message.actor);
            rsize = engine.ready.size();
        }
    }
}