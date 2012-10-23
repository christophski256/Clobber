package net.foxopen.clobber.params;

import java.util.HashMap;
import java.util.Map;

public class YesNoCancelType{
  public static final int YES = 1;
  public static final int CANCEL =2;
  public static final int NO =3;
  private static final String yesString= "Yes";
  private static final String noString= "No";
  private static final String cancelString= "Cancel";
  private static Map<String,Integer> OPTION_MAPPING;
  
  private int value;


  public YesNoCancelType(int value) {
    this.value = value;
  }
  public YesNoCancelType(String stringRepresentation){
    this.value = OPTION_MAPPING.get(stringRepresentation);
  }

  public int getValue() {
    return this.value;
  }
  @Override
  public String toString(){
    return ""+this.value;
  }

  public static String[] getOrderedOptions() {
    String[] options = {yesString,noString,cancelString};
    return options;    
  }
  
  static{
    OPTION_MAPPING = new HashMap<String,Integer>();
    OPTION_MAPPING.put(yesString, YES);
    OPTION_MAPPING.put(noString, NO);
    OPTION_MAPPING.put(cancelString, CANCEL);
  }

}
