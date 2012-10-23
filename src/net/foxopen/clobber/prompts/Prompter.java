package net.foxopen.clobber.prompts;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class Prompter {
  
  private static Prompter PLP = null;
 
  
  public static Prompter getPrompter(){
    if(PLP==null){
      PLP = new Prompter();
    }
    return PLP;
  }
  
  public Prompter(){
    this.logger = Logger.getLogger(this.getClass());
  }
  
  
  private JFrame parentFrame;
  private Logger logger;

  
  
  public void setFrame (JFrame frame){
    this.parentFrame = frame;
  }
  
  
  public String getOption(String[] options , String prompt){
    this.logger.debug("Prompting");
    int selected = JOptionPane.showOptionDialog(this.parentFrame, prompt, "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    this.logger.debug("Selected Option \"" + options[selected]+"\"");
    return options[selected];    
  }


  public boolean getBoolean(String prompt) {
    int result = JOptionPane.showConfirmDialog(this.parentFrame,prompt, null, JOptionPane.YES_NO_OPTION);
    this.logger.debug("Result is "+ result);
    return result==JOptionPane.YES_OPTION;
  }
  
  
   
  
}
