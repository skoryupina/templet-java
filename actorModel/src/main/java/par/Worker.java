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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Поток поддерживающий работу актора.
 */
class Worker implements Runnable {
    private Logger LOG = LoggerFactory.getLogger(Worker.class);
    private Message message;

    Worker(@NotNull Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        if (message != null) {
            LOG.debug("Запустили Worker'a:" + message.toString());
            message.setSending(false);
            @Nullable Actor actor = message.getActor();

            assert actor != null;
            actor.recv(message);
        }
    }
}

