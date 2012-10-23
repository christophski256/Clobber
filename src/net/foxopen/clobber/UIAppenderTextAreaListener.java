package net.foxopen.clobber;

import javax.swing.JTextArea;

class UIAppenderTextAreaListener extends JTextArea implements UIAppenderListener {

  /**
   * Whatever
   */
  private static final long serialVersionUID = -7684167236618958802L;

  public UIAppenderTextAreaListener(UIAppender appender) {
    try {
      appender.addListener(this);
    } catch (NullPointerException e) {
      this.setText("There was an issue setting the log writer.");
    }

    this.setEditable(false);

  }

  @Override
  public void update(Object message) {
    this.setText(this.getText() + "\n" + message);
    this.repaint();

  }

}