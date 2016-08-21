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
