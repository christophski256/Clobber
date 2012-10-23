package net.foxopen.clobber.ui.projectTable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.foxopen.clobber.clobberResource.ClobberResource;


public class ResourceTableModel implements TableModel {
  
  private List<ClobberResource> resources;
  private List<ResourceTableColumn> columns;
  
  public ResourceTableModel(List<ClobberResource> resources){
    this.resources = resources;
    this.columns = new ArrayList<ResourceTableColumn>();
    this.columns.add(new ActiveColumn());
    this.columns.add(new FileNameColumn());
    this.columns.add(new ClobberConnectionTypeColumn());
    this.columns.add(new DateModifiedColumn());
    this.columns.add(new DateLastClobbedColumn());
    
  }
  
  
  @Override
  public int getRowCount() {
    return this.resources.size();
  }

  @Override
  public int getColumnCount() {
    return this.columns.size();
  }

  @Override
  public String getColumnName(int columnIndex) {
    return this.columns.get(columnIndex).getColumnName();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return this.columns.get(columnIndex).getColumnClass();
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return this.columns.get(columnIndex).isEditable();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return this.columns.get(columnIndex).getValueFor(this.resources.get(rowIndex));
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    this.columns.get(columnIndex).setValueOfResource(this.resources.get(rowIndex), aValue);
  }

  @Override
  public void addTableModelListener(TableModelListener l) {
    
  }

  @Override
  public void removeTableModelListener(TableModelListener l) {
  }
}
