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
package gauss_seidel;

import org.junit.Before;
import org.junit.Test;

public class SequentialVersionTest {
    private static final String FILENAME = "matrix.dat";
    private InputMatrixHelper inputMatrixHelper;

    @Before
    public void beforeTests() {
        inputMatrixHelper = InputMatrixHelper.deserializeMatrix(FILENAME);
    }

    @Test
    public void isCalculated() {
        assert inputMatrixHelper != null;
        double[][] result = SequentialVersion.calculateSequentially(inputMatrixHelper.getU());
        InputMatrixHelper resultMatrix = new InputMatrixHelper(result);
        InputMatrixHelper.serializeMatrix("sequentialResult.dat", resultMatrix);
    }
}
