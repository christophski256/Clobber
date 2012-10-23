package net.foxopen.clobber.params;

import java.util.HashMap;
import java.util.Map;

import nu.xom.Element;

import org.apache.log4j.Logger;

/**
 * A container for Clobbing rule parameters.
 *
 * @author aled
 *
 */
public class ClobRuleParams {

  /**
   * Should a clob happen on readonly?
   */
  public static final String CLOB_ON_READONLY = "CLOB_ON_READONLY";
  /**
   * Should a clob happen on a difference between the last clobbed version and
   * the version in the remote datastore?
   * 
   */
  public static final String CLOB_ON_DIFFERENCE = "CLOB_ON_DIFFERENCE";
  /**
   * Should clobbing happen?
   */
  public static final String CLOB = "CLOB";

  private Map<String, PromptYesNoType> paramMap;

  public ClobRuleParams() {
    this.paramMap = new HashMap<String, PromptYesNoType>();
  }

  public boolean addParam(String paramName, PromptYesNoType value) {
    Logger l = Logger.getLogger(ClobRuleParams.class);
    l.debug("param name "+paramName + " value "+value);
    
    boolean added = false;
    
    if (!this.paramMap.containsKey(paramName)) {
      l.debug("adding");
      this.paramMap.put(paramName, value);
      added = true;
    }
    return added;
  }

  /**
   * Returns the value for the parameter given. If no match is found; null is
   * returned.
   * 
   * @param clobOnDifference
   *          The ClobRuleParams parameter name matches static ints in
   *          ClobRuleParams.
   * @return The PromptYesNo value.
   */
  public PromptYesNoType getValue(String clobOnDifference) {
    return this.paramMap.get(clobOnDifference);
  }

  public Element getRulesElement() {
    Logger l = Logger.getLogger(this.getClass());
    Element rulesElement = new Element("CLOB_RULE_LIST");
    for(Map.Entry<String,PromptYesNoType> ruleParam : this.paramMap.entrySet()){
      Element clobRuleElement = new Element("CLOB_RULE");
      Element ruleName = new Element("RULE_NAME");
      ruleName.appendChild( ruleParam.getKey());
      clobRuleElement.appendChild(ruleName);
      Element value = new Element("VALUE");
      value.appendChild(ruleParam.getValue().getValue());
      
      clobRuleElement.appendChild(value);
      rulesElement.appendChild(clobRuleElement);
    }
    l.debug("Ruleset serialised to \n"+rulesElement.toXML());
    return rulesElement;
  }
}
