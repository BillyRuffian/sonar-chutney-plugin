package uk.billy_ruffian.sonar.plugins.chutney.sensors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.utils.command.Command;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.api.utils.command.StringStreamConsumer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import uk.billy_ruffian.sonar.plugins.chutney.languages.GherkinLanguage;
import uk.billy_ruffian.sonar.plugins.chutney.rules.GherkinRulesDefinition;
import uk.billy_ruffian.sonar.plugins.chutney.settings.ChutneySettings;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GherkinChutneySensor implements Sensor {

    private static final Logger LOG = Loggers.get(GherkinChutneySensor.class);

    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor
                .onlyOnLanguage(GherkinLanguage.KEY)
                .name("Gherkin Chutney Sensor")
                .onlyOnFileType(InputFile.Type.MAIN);
    }

    @Override
    public void execute(SensorContext sensorContext) {
        String exe = sensorContext
                .config()
                .get(ChutneySettings.CHUTNEY_EXECUTABLE)
                .orElse("chutney");
        LOG.info("Chutney plugin: assuming executable is " + exe);

        FileSystem fs = sensorContext.fileSystem();

        Iterable<InputFile> fileIterable = fs.inputFiles(fs.predicates().hasLanguage(GherkinLanguage.KEY));
        List<String> filenamesList = StreamSupport.stream(
                fileIterable.spliterator(), false)
                .map(f -> f.toString())
                .collect(Collectors.toList());
        String filenames = String.join(" ", filenamesList);

        Command chutney = Command.create(exe).addArgument("-fJSONFormatter");
        for (String feature : filenamesList) {
            chutney.addArgument(feature);
        }
        StringStreamConsumer stdOut = new StringStreamConsumer();
        StringStreamConsumer stdErr = new StringStreamConsumer();
        CommandExecutor.create().execute(chutney, stdOut, stdErr, 10000);

        LOG.info("Chutney scanned " + filenamesList.size() + " feature files");

        if (!stdErr.getOutput().equals("")) {
            LOG.error("Chutney reported error during scan: " + stdErr.getOutput());
        }

        JSONObject resultsJSON = new JSONObject(stdOut.getOutput());
        for (InputFile feature : fs.inputFiles(fs.predicates().hasLanguage(GherkinLanguage.KEY))) {
            for (Object lintersObj : resultsJSON.getJSONArray(feature.toString())) {
                JSONObject linter = (JSONObject) lintersObj;
                JSONArray issues = linter.getJSONArray("issues");
                for (Object o : issues) {
                    JSONObject issue = (JSONObject) o;
                    raiseIssue(sensorContext, feature, linter, issue);
                }
            }
        }
    }

    private void raiseIssue(SensorContext sensorContext, InputFile feature, JSONObject linter, JSONObject issue) {
        NewIssue newIssue = sensorContext.newIssue()
                .forRule(GherkinRulesDefinition.CHUTNEY_VIOLATION)
                // gap is used to estimate the remediation cost to fix the debt
                .gap(1.0);

        String linterName = humanizeLinterName(linter.getString("linter"));
        NewIssueLocation primaryLocation = newIssue.newLocation()
                .on(feature)
                .at(feature.selectLine(issue.getJSONObject("location").getInt("line")))
                .message(linterName);
        newIssue.at(primaryLocation);
        newIssue.save();
    }

    private String humanizeLinterName(String linter) {
        String niceLinterName = linter.replaceAll("([^_])([A-Z])", "$1 $2").toLowerCase();
        return niceLinterName.substring(0, 1).toUpperCase() + niceLinterName.substring(1);
    }

}
