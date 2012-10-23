package net.foxopen.clobber.ui.projectTable;

import net.foxopen.clobber.clobberResource.ClobberResource;

import org.apache.log4j.Logger;

public class ActiveColumn extends ResourceTableColumn {
  @Override
  public String getColumnNameDeferred() {
    return "Active";
  }

  @Override
  public Class<?> getColumnClassDeferred() {
    return Boolean.class;
  }

  @Override
  public boolean isEditable() {
    return true;
  }

  @Override
  public Object getValueFor(ClobberResource clobberResource) {
    return clobberResource.isActive();
  }

  @Override
  public void setValueOfResource(ClobberResource clobberResource, Object object) {
    Logger l = Logger.getLogger(ActiveColumn.class);
    l.debug("Casting "+object+" to boolean");
    boolean b =(Boolean) object;
    l.info("Setting activity of "+clobberResource.getResourceName()+" to "+object );
    clobberResource.setActive(b);
  }
}
