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
        for (int i = 1; i < InputMatrixHelper.SIZE_X; i++) {
            for (int j = 1; j < InputMatrixHelper.SIZE_Y; j++) {
                buffer.append(u[i][j]).append("  ");
            }
            LOG.debug(buffer.toString());
            buffer.delete(0, buffer.capacity() - 1);
        }
    }
}
