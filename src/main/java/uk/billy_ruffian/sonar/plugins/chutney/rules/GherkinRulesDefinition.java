/*
 * Example Plugin for SonarQube
 * Copyright (C) 2009-2020 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package uk.billy_ruffian.sonar.plugins.chutney.rules;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;
import uk.billy_ruffian.sonar.plugins.chutney.languages.GherkinLanguage;

public final class GherkinRulesDefinition implements RulesDefinition {

    protected static final String KEY = "cucumberchutney";
    protected static final String NAME = "CucumberChutney";

    public static final String REPO_KEY = GherkinLanguage.KEY + "-" + KEY;
    protected static final String REPO_NAME = GherkinLanguage.KEY + "-" + NAME;
    public static final RuleKey CHUTNEY_VIOLATION = RuleKey.of(REPO_KEY, "Chutney Violation");

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(REPO_KEY, GherkinLanguage.KEY).setName("Gherkin Analyzer (Chutney)");

        NewRule x1Rule = repository.createRule(CHUTNEY_VIOLATION.rule())
                .setName("Chutney violation")
                .setHtmlDescription("Chutney reports a violation, run chutney locally for more details of the the issue.")

                // optional tags
                .setTags("style", "chutney")

                // default severity when the rule is activated on a Quality profile. Default value is MAJOR.
                .setSeverity(Severity.MAJOR);

        x1Rule.setDebtRemediationFunction(x1Rule.debtRemediationFunctions().linearWithOffset("1h", "0min"));

        repository.done();
    }

}
