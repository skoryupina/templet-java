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

/**
 * Средство передачи знаний между акторами.
 */
public class Message {
    private Logger LOG = LoggerFactory.getLogger(Message.class);

    /**
     * Актор-получатель сообщения.
     */
    public Actor actor;

    /**
     * Статус сообщения.
     */
    public boolean sending;

    /**
     * Передать сообщение в очередь сообщений.
     *
     * @param engine Движок - среда обитания акторов.
     * @param actor  Получатель сообщения.
     */
    public void send(Engine engine, Message message, Actor actor) {
        if (message.sending) {
            return;
        }
        synchronized (engine.getReady()) {
            message.sending = true;
            message.actor = actor;
            engine.getReady().add(message);
            LOG.debug("Добавили в очередь message");
        }
    }

    /**
     * Проверка доступа актора к сообщению.
     * Актор должен совпадать с получателем,
     * сообщение должно быть доставленным
     *
     * @param actor   Проверяемый актор.
     * @param message Проверяемое сообщение.
     * @return Есть ли право доступа.
     */
    public boolean access(Message message, Actor actor) {
        return message.actor == actor && !message.sending;
    }

    @Override
    public String toString() {
        return this.getClass() +
                "actor=" + actor +
                ", sending=" + sending +
                '}';
    }
}
