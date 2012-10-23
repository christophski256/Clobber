package net.foxopen.clobber.ui.projectTable;

import net.foxopen.clobber.clobberResource.ClobberResource;

public abstract class ResourceTableColumn {

  public String getColumnName(){
    return this.getColumnNameDeferred();
  }

  public abstract String getColumnNameDeferred();


  public Class<?> getColumnClass(){
    return this.getColumnClassDeferred();
  };
  
  public abstract Class<?> getColumnClassDeferred();

  public abstract boolean isEditable();

  public abstract Object getValueFor(ClobberResource clobberResource);

  public abstract void setValueOfResource(ClobberResource clobberResource, Object object);


}
