package net.foxopen.clobber;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class UIAppender extends AppenderSkeleton {

  private List<UIAppenderListener> listeners;

  public UIAppender() {
    super();
    this.listeners = new ArrayList<UIAppenderListener>();
  }

  public void addListener(UIAppenderListener ual) {
    this.listeners.add(ual);
  }

  @Override
  protected void append(LoggingEvent event) {
    for (UIAppenderListener ual : this.listeners) {

      ual.update(this.layout.format(event));
    }

  }

  @Override
  public void close() {
    this.listeners = null;

  }

  @Override
  public boolean requiresLayout() {
    return true;
  }

}
