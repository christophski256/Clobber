package net.foxopen.clobber.ui.projectTable;

import net.foxopen.clobber.clobberResource.ClobberResource;

public class ClobberConnectionTypeColumn extends ResourceTableColumn {
  @Override
  public String getColumnNameDeferred() {
    return "Connection Type";
  }

  @Override
  public Class<?> getColumnClassDeferred() {
    return String.class;
  }

  @Override
  public boolean isEditable() {
    return false;
  }

  @Override
  public Object getValueFor(ClobberResource clobberResource) {
    return clobberResource.getResourceConnectionDescription();
  }

  @Override
  public void setValueOfResource(ClobberResource clobberResource, Object object) {
    throw new UnsupportedOperationException("You can't change the value of this column through the GUI");
  }
}
