package net.foxopen.clobber.params;


/**
 * Yes/No/Prompt option
 * 
 * @author aled
 * 
 */
public class PromptYesNoType {

  public static final String YES = "YES";
  public static final String PROMPT = "PROMPT";
  public static final String NO = "NO";

  private String value;


  public PromptYesNoType(String stringValue) {
    this.value = stringValue;
  }

  public String getValue() {
    return this.value;
  }
  @Override
  public String toString(){
    return this.value;
  }

}
