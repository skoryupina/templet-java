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

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Random;

public class InputMatrixHelper implements Serializable {

    static Logger LOG = LoggerFactory.getLogger(InputMatrixHelper.class);

//    public static final int SIZE_X = 800;
//    public static final int SIZE_Y = 80;
//    public static final int ITERATIONS = 10000;

    public static final int SIZE_X = 10;
    public static final int SIZE_Y = 10;
    public static final int ITERATIONS = 2;
    private double[][] u;

    public InputMatrixHelper() {
    }

    public InputMatrixHelper(double[][] result) {
        u = result.clone();
    }

    public double[][] getU() {
        return u;
    }

    public void init() {
        u = new double[SIZE_X][SIZE_Y];
        Random random = new Random();
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                u[i][j] = random.nextDouble();
            }
        }
    }

    public static void serializeMatrix(String fileName, InputMatrixHelper matrixHelper) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(matrixHelper);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    public static @Nullable InputMatrixHelper deserializeMatrix(String fileName) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            return (InputMatrixHelper) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }
}
