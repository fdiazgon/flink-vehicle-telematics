import fediazgon.AccidentReporter;
import fediazgon.AverageSpeedControl;
import fediazgon.SpeedRadar;
import fediazgon.VehicleTelematics;
import org.apache.flink.runtime.client.JobExecutionException;
import org.apache.flink.test.util.AbstractTestBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static fediazgon.VehicleTelematics.*;
import static org.junit.Assert.assertTrue;

public class MainTests extends AbstractTestBase {

    private static final String INPUT_FILE = "test_input.csv";
    private static final String OUTPUT_FOLDER = "output";

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();


    @Test(expected = Exception.class)
    public void shouldPrintHelpMessageAndExit() throws Exception {
        VehicleTelematics.main(new String[0]);
    }

    @Test(expected = JobExecutionException.class)
    public void inputFileDoesNotExists() throws Exception {
        String output = temporaryFolder.newFolder(OUTPUT_FOLDER).getPath();
        VehicleTelematics.main(new String[]{"whatever", output});
    }

    @Test
    public void shouldRunMainAndCreateFiles() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String input = new File(classLoader.getResource(INPUT_FILE).getFile()).getPath();
        String output = temporaryFolder.newFolder(OUTPUT_FOLDER).getPath();
        String[] args = new String[]{input, output};

        VehicleTelematics.main(args);

        assertTrue(new File(String.format("%s/%s", output, SPEED_RADAR_FILE)).exists());
        assertTrue(new File(String.format("%s/%s", output, AVG_SPEED_FILE)).exists());
        assertTrue(new File(String.format("%s/%s", output, ACCIDENTS_FILE)).exists());
    }

    //TODO: delete and make private constructors when jacoco 0.8 is released
    @Test
    public void hackConstructorCoverage() {
        new VehicleTelematics();
        new SpeedRadar();
        new AverageSpeedControl();
        new AccidentReporter();
    }

}
