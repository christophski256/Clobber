package net.foxopen.clobber.clobberResource.clobberConnection;

import java.awt.Window;

import javax.swing.JPanel;

public abstract class AddResourcePanel extends JPanel {
  /**
   * Whatever
   */
  
  private Window parentWindow;
  
  private static final long serialVersionUID = 1L;

  public abstract String getTitle();
  
  public void setParentWindow(Window w) {
    this.parentWindow = w;
  }
  
  public Window getParentWindow() {
    return this.parentWindow;
  }
  
}
