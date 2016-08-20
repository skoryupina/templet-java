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

package sim;


import java.util.LinkedList;
import java.util.Queue;

public class tet {
    static class Event {
        double time;

        enum Type {
            CHAN, PROC0, PROC1
        }

        Type type;
        Message message;
        Actor actor;
    }

    class Comparator {
        public boolean compare(Event event1, Event event2) {
            return event1.time > event2.time;
        }
    }

	/*class QueueTriple{
        Event event;
		Queue<Event> eventQueue;
		Comparator cmp;
	}*/

    class Engine {
        Queue<Event> calendar;
        double Tp;
        double T1;
        int Pmax;
    }

    abstract class Actor {
        boolean lock;
        Queue<Message> ready = new LinkedList<Message>();

        abstract void recv(Message message, Actor actor);
    }

    class Message {
        Actor actor;
        boolean sending;

        public void duration(Engine engine, double t) {
            engine.T1 += t;
            engine.Tp += t;
        }

        public void send(Engine engine, Message message, Actor actor) {
            if (message.sending) {
                return;
            }
            message.sending = true;
            message.actor = actor;
            Event event = new Event();
            event.time = engine.Tp;
            event.type = Event.Type.CHAN;
            event.message = message;
            engine.calendar.add(event);
        }

        public boolean access(Message message, Actor actor) {
            return message.actor == actor && !message.sending;
        }
    }

    public void run(Engine engine) {
        int n = 1;
        Actor actor = null;
        Message message = null;
        double Tcur = 0.0, Tprev = 0.0;
        int Pcur = 0, Pmax = 0;
        while (!engine.calendar.isEmpty()) {
            Event event;
            event = engine.calendar.poll();
            Tcur = event.time;
            if (Tcur - Tprev > 0 && Pcur > Pmax) {
                Pmax = Pcur;
            }
            Tprev = Tcur;
            switch (event.type) {
                case CHAN: {
                    actor = event.message.actor;
                    if (!actor.lock && actor.ready.isEmpty()) {
                        event.actor = actor;
                        event.type = Event.Type.PROC0;
                        engine.calendar.add(event);
                    }
                    actor.ready.add(event.message);
                }
                break;
                case PROC0: {
                    actor = event.actor;
                    message = actor.ready.poll();
                    message.sending = false;
                    actor.lock = true;
                    Pcur++;
                    engine.Tp = Tcur;
                    actor.recv(message, actor);
                    Tcur = engine.Tp;
                    event.time = Tcur;
                    event.type = Event.Type.PROC1;
                    engine.calendar.add(event);
                }
                break;
                case PROC1: {
                    actor = event.actor;
                    if (!actor.ready.isEmpty()) {
                        event.type = Event.Type.PROC0;
                        engine.calendar.add(event);
                    }
                    actor.lock = false;
                    Pcur--;
                }
                break;
            }
        }
        engine.Tp = Tcur;
        engine.Pmax = Pmax;
    }

    void stat(Engine engine, double T1, double Tp, int Pmax, double Smax, int P, double Sp) {
        T1 = engine.T1;
        Tp = engine.Tp;
        Pmax = engine.Pmax;
        Smax = T1 / Tp;
        double alpha = (1 - Smax / Pmax) / (Smax - Smax / Pmax);
        Sp = (P > Pmax) ? Smax : 1 / (alpha + (1 + alpha) / P);
    }
}