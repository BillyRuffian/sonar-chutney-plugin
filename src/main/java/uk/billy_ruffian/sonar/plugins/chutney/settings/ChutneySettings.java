package uk.billy_ruffian.sonar.plugins.chutney.settings;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import uk.billy_ruffian.sonar.plugins.chutney.languages.GherkinLanguage;

import java.util.List;

import static java.util.Arrays.asList;

public class ChutneySettings {

    public static final String CHUTNEY_EXECUTABLE = "sonar.chutney.executable";

    private ChutneySettings() {
    }

    public static List<PropertyDefinition> getProperties() {
        return asList(
                PropertyDefinition.builder(CHUTNEY_EXECUTABLE)
                        .category("Gherkin (Chutney)")
                        .name("Chutney Executable")
                        .description("The location of the chutney linter executable.")
                        .defaultValue("chutney")
                        .onQualifiers(Qualifiers.PROJECT)
                        .build());
    }

}
