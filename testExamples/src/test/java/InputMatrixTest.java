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
import gauss_seidel.InputMatrixHelper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputMatrixTest {

    private static Logger LOG = LoggerFactory.getLogger(InputMatrixTest.class);
    private static final String FILENAME = "matrix.dat";

    @Before
    public void beforeTests() {
        InputMatrixHelper matrix = new InputMatrixHelper();
        matrix.init();
        InputMatrixHelper.serializeMatrix(FILENAME, matrix);
    }


    @Test
    public void isDeserialized() {
        InputMatrixHelper matrixHelper = InputMatrixHelper.deserializeMatrix(FILENAME);
        assert matrixHelper != null;
        printResults(matrixHelper.getU());
    }


    private void printResults(double[][] u) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < InputMatrixHelper.SIZE_X; i++) {
            for (int j = 0; j < InputMatrixHelper.SIZE_Y; j++) {
                buffer.append(u[i][j]).append("  ");
            }
            LOG.debug(buffer.toString());
            buffer.delete(0, buffer.capacity() - 1);
        }
    }
}
