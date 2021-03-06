package net.foxopen.clobber.ui.projectTable;

import java.util.Date;

import net.foxopen.clobber.clobberResource.ClobberResource;

public class DateLastClobbedColumn extends ResourceTableColumn{
  @Override
  public String getColumnNameDeferred() {
    return "Last Clobbed";
  }

  @Override
  public Class<?> getColumnClassDeferred() {
    return Date.class;
  }

  @Override
  public boolean isEditable() {
    return false;
  }

  @Override
  public Object getValueFor(ClobberResource clobberResource) {
    return clobberResource.getLastClobbed();
  }

  @Override
  public void setValueOfResource(ClobberResource clobberResource, Object object) {
    throw new UnsupportedOperationException("You can't change the value of this column through the GUI");
  }
}
