package uk.billy_ruffian.sonar.plugins.chutney.languages;

import org.sonar.api.resources.AbstractLanguage;

public class GherkinLanguage extends AbstractLanguage {
    public static final String KEY = "gherkin";

    public GherkinLanguage() {
        super(KEY, "Cucumber Gherkin");
    }

    @Override
    public String[] getFileSuffixes() {
        return new String[] {"feature"};
    }
}
