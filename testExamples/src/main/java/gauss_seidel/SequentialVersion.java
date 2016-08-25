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

public class SequentialVersion {

    public static void operation(int i, double[][] u) {
        for (int j = 1; j < InputMatrixHelper.SIZE_Y - 1; j++)
            u[i][j] = (u[i][j - 1] + u[i][j + 1] + u[i - 1][j] + u[i + 1][j]) * 0.25;
    }

    public static double[][] calculateSequentially(double[][] u) {
        for (int t = 1; t <= InputMatrixHelper.ITERATIONS; t++)
            for (int i = 1; i < InputMatrixHelper.SIZE_X - 1; i++)
                operation(i, u);

        return u;
    }
}
