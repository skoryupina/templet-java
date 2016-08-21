package gauss_seidel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParallelVersion {

    Logger LOG = LoggerFactory.getLogger(ParallelVersion.class);

    public static void operation(int i, double[][] u) {
        for (int j = 1; j < InputMatrixHelper.SIZE_Y - 1; j++)
            u[i][j] = (u[i][j - 1] + u[i][j + 1] + u[i - 1][j] + u[i + 1][j]) * 0.25;
    }

    public static double[][] calculateParSequentially(double[][] u) {
        int t = 1;

        class ParallelFor1 implements Runnable {
            public void run() {
                for (int t = 1; t <= (2 * InputMatrixHelper.ITERATIONS - 1) + (InputMatrixHelper.SIZE_X - 3); t++) {
                    if (t % 2 == 1) {
                        for (int i = 1; i < InputMatrixHelper.SIZE_X - 1; i += 2) {
                            if (i <= t && i > t - 2 * InputMatrixHelper.ITERATIONS)
                                operation(i, u);
                        }
                    }
                }
            }
        }

        class ParallelFor2 implements Runnable {

            public void run() {
                for (int t = 1; t <= (2 * InputMatrixHelper.ITERATIONS - 1) + (InputMatrixHelper.SIZE_X - 3); t++) {
                    if (t % 2 == 0) {
                        for (int i = 2; i < InputMatrixHelper.SIZE_X - 1; i += 2) {
                            if (i <= t && i > t - 2 * InputMatrixHelper.ITERATIONS) {
                                operation(i, u);
                            }
                        }
                    }
                }
            }
        }
        Thread par1 = new Thread(new ParallelFor1());
        Thread par2 = new Thread(new ParallelFor2());
        par1.start();
        par2.start();

        try {
            par1.join();
            par2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return u;
    }
}