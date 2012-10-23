package net.foxopen.clobber.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import net.foxopen.clobber.ClobberModel;

import org.apache.log4j.Logger;

public class AddProjectListener implements ActionListener {

  private Logger logger;
  private JFrame component;
  private ClobberModel model;

  public AddProjectListener(JFrame comp, ClobberModel model){
    this.logger = Logger.getLogger(this.getClass());
    this.model = model;
    this.component  = comp;
  }


  @Override
  public void actionPerformed(ActionEvent arg0) {
    this.logger.debug("Button Clicked");
    JFileChooser chooser = new JFileChooser();
    
    int result = chooser.showDialog(this.component, "Open");
    if(result == JFileChooser.APPROVE_OPTION){
      this.model.openProject(chooser.getSelectedFile());
    }
    
    
  }
  

}
