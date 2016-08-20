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

import org.jetbrains.annotations.Nullable;

/**
 * Средство передачи знаний между акторами.
 */
public class Message {
    /**
     * Актор-отправитель сообщения.
     */
    private Actor actor;

    /**
     * Статус сообщения.
     */
    private boolean sending;

    /**
     * Передать сообщение.
     *
     * @param engine Движок - среда обитания акторов.
     * @param actor  Получатель сообщения.
     */
    public void send(Engine engine, Actor actor) {
        engine.getReady().add(this);
        this.sending = true;
        this.actor = actor;
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
    public boolean access(Actor actor, Message message) {
        return message.actor == actor && !message.sending;
    }

    /**
     * Получить актора сообщения.
     */
    @Nullable
    Actor getActor() {
        return actor;
    }

    /**
     * После обработки сообщения, разрываем связь с актором.
     */
    public void setActor(@Nullable Actor actor) {
        this.actor = actor;
    }

    void setSending(boolean sending) {
        this.sending = sending;
    }
}
