package net.foxopen.clobber.ui.projectTable;

import javax.swing.JTable;
import java.util.List;

import net.foxopen.clobber.clobberResource.ClobberResource;

public class ResourceTable extends JTable {
  private static final long serialVersionUID = -1L;
  private List<ClobberResource> resources;
  
  
  public ResourceTable(List<ClobberResource> resources){
    this.resources=resources;
    this.setModel(new ResourceTableModel(resources));
    this.setAutoCreateRowSorter(true);
  }
  
}
