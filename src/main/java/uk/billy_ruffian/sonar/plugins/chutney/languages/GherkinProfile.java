package uk.billy_ruffian.sonar.plugins.chutney.languages;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import uk.billy_ruffian.sonar.plugins.chutney.rules.GherkinRulesDefinition;

public class GherkinProfile implements BuiltInQualityProfilesDefinition {

    @Override
    public void define(Context context) {
        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("Gherkin Rules", GherkinLanguage.KEY);
        profile.setDefault(true);

        NewBuiltInActiveRule rule1 = profile.activateRule(GherkinRulesDefinition.REPO_KEY, "Chutney Violation");
//        rule1.overrideSeverity("BLOCKER");

        profile.done();
    }
}
