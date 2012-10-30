package net.foxopen.clobber.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CloseActionListener implements ActionListener {

  private JPanel p;
  
  public CloseActionListener(JPanel p) {
     this.p = p;
  }
  
  public void actionPerformed(ActionEvent e) {
    SwingUtilities.getWindowAncestor(p).dispose();
  }

}
