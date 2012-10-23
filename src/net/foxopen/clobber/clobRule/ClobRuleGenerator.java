package net.foxopen.clobber.clobRule;

import static net.foxopen.clobber.params.ClobRuleParams.CLOB;
import static net.foxopen.clobber.params.ClobRuleParams.CLOB_ON_DIFFERENCE;
import static net.foxopen.clobber.params.ClobRuleParams.CLOB_ON_READONLY;
import static net.foxopen.clobber.params.PromptYesNoType.PROMPT;
import net.foxopen.clobber.params.ClobRuleParams;
import net.foxopen.clobber.params.PromptYesNoType;

import nu.xom.Element;

/**
 * Generates a ClobRule based on some parameters
 *
 * @author aled
 *
 */
public class ClobRuleGenerator {

  public static final String PROMPT_TO_CLOB = "Do you want to clob?";
  public static final String PROMPT_TO_CLOB_DIFFERENT = "This file has changed. Are you sure you want to clob?";
  public static final String PROMPT_TO_CLOB_READONLY = "This file is readonly. Are you sure you want to clob?";

  private ClobRuleParams clobRuleParams;

  /**
   * Constructor to create a clob generator for using a set of parameters
   * 
   * @param params
   *          a ClobRuleParams object describing the clobbing rules
   */
  public ClobRuleGenerator(ClobRuleParams params) {
    this.clobRuleParams = params;
  }

  /**
   * Generate a ClobRule which determines whether or not a resource should be
   * clobbed.
   * 
   * @param provider
   *          Information needed to generate the rules.
   * @return A clob rule to determine whether to clob or not
   */
  public ClobRule generateClobRule(ClobRuleProvider provider) {
    ClobRule rule = new BaseClobRule();
    // look for prompt to clob

    PromptYesNoType actionToTake = this.clobRuleParams.getValue(CLOB);

    if (actionToTake != null) {
      switch (actionToTake.getValue()) {
      case PROMPT:
        rule = new PromptOnlyClobRule(PROMPT_TO_CLOB, provider, rule);
        break;
      }
    }
    // look for prompt on difference detected
    actionToTake = this.clobRuleParams.getValue(CLOB_ON_DIFFERENCE);

    if (actionToTake != null) {
      switch (actionToTake.getValue()) {

      case PromptYesNoType.PROMPT:
        rule = new PromptDifferentClobRule(PROMPT_TO_CLOB_DIFFERENT, provider, rule);
        break;

      case PromptYesNoType.NO:
        rule = new DifferentClobRule(provider, rule);
        break;
      }
    }

    // look for prompt on readonly
    actionToTake = this.clobRuleParams.getValue(CLOB_ON_READONLY);
    if (actionToTake != null) {
      switch (actionToTake.getValue()) {

      case PromptYesNoType.PROMPT:
        rule = new PromptReadOnlyClobRule(PROMPT_TO_CLOB_READONLY, provider, rule);
        break;

      case PromptYesNoType.NO:
        rule = new ReadOnlyClobRule(provider, rule);
        break;
      }
    }
    return rule;
  }

  /**
   * Sets the params for the generator
   * 
   * @param params
   */
  public void setParams(ClobRuleParams params) {
    this.clobRuleParams = params;
  }

  public Element getClobRulesElement() {
    
    return this.clobRuleParams.getRulesElement();
  }
}
