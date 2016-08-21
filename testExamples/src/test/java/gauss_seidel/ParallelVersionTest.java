package gauss_seidel;

import org.junit.Before;
import org.junit.Test;

public class ParallelVersionTest {
    private static final String FILENAME = "matrix.dat";
    private InputMatrixHelper inputMatrixHelper;


    @Before
    public void beforeTests() {
        inputMatrixHelper = InputMatrixHelper.deserializeMatrix(FILENAME);
    }

    @Test
    public void isCalculated() {
        assert inputMatrixHelper != null;
        double[][] result = ParallelVersion.calculateParSequentially(inputMatrixHelper.getU());
        InputMatrixHelper resultMatrix = new InputMatrixHelper(result);
        InputMatrixHelper.serializeMatrix("parallelResult.dat", resultMatrix);
    }
}
