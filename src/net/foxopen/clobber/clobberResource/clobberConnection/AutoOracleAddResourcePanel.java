package net.foxopen.clobber.clobberResource.clobberConnection;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import net.foxopen.clobber.ui.CloseActionListener;


public class AutoOracleAddResourcePanel extends  AddResourcePanel {


  
  /**
   * Create the panel.
   */
  public AutoOracleAddResourcePanel() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{141, 28, 131, 0};
    gridBagLayout.rowHeights = new int[]{26, 26,26,26};
    gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{1, 1,1,1};
    setLayout(gridBagLayout);
    
    JLabel lblConfigLocation = new JLabel("Config Location");
    GridBagConstraints gbc_lblConfigLocation = new GridBagConstraints();
    gbc_lblConfigLocation.insets = new Insets(0, 0, 5, 5);
    gbc_lblConfigLocation.anchor = GridBagConstraints.EAST;
    gbc_lblConfigLocation.gridx = 0;
    gbc_lblConfigLocation.gridy = 0;
    add(lblConfigLocation, gbc_lblConfigLocation);
    
    JComboBox configLocationComboBox = new JComboBox();
    configLocationComboBox.setEditable(true);
    GridBagConstraints gbc_configLocationComboBox = new GridBagConstraints();
    gbc_configLocationComboBox.anchor = GridBagConstraints.WEST;
    gbc_configLocationComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_configLocationComboBox.gridx = 1;
    gbc_configLocationComboBox.gridy = 0;
    add(configLocationComboBox, gbc_configLocationComboBox);
    
    JButton addNewConfigLocationComboBox = new JButton("New Config Location");
    GridBagConstraints gbc_addNewConfigLocationComboBox = new GridBagConstraints();
    gbc_addNewConfigLocationComboBox.anchor = GridBagConstraints.WEST;
    gbc_addNewConfigLocationComboBox.insets = new Insets(0, 0, 5, 0);
    gbc_addNewConfigLocationComboBox.gridx = 2;
    gbc_addNewConfigLocationComboBox.gridy = 0;
    add(addNewConfigLocationComboBox, gbc_addNewConfigLocationComboBox);
    
    JLabel lblTableTypeInstance = new JLabel("Table Type Instance");
    GridBagConstraints gbc_lblTableTypeInstance = new GridBagConstraints();
    gbc_lblTableTypeInstance.insets = new Insets(0, 0, 5, 5);
    gbc_lblTableTypeInstance.anchor = GridBagConstraints.EAST;
    gbc_lblTableTypeInstance.gridx = 0;
    gbc_lblTableTypeInstance.gridy = 1;
    add(lblTableTypeInstance, gbc_lblTableTypeInstance);
    
    
    
    JComboBox tableTypeInstanceComboBox = new JComboBox();
    GridBagConstraints gbc_tableTypeInstanceComboBox = new GridBagConstraints();
    gbc_tableTypeInstanceComboBox.anchor = GridBagConstraints.WEST;
    gbc_tableTypeInstanceComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_tableTypeInstanceComboBox.gridx = 1;
    gbc_tableTypeInstanceComboBox.gridy = 1;
    add(tableTypeInstanceComboBox, gbc_tableTypeInstanceComboBox);
    
    JButton createButton = new JButton("Add");
    createButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      }
    });
    GridBagConstraints gbc_createButton = new GridBagConstraints();
    gbc_createButton.insets = new Insets(0, 0, 5, 5);
    gbc_createButton.gridx = 1;
    gbc_createButton.gridy = 2;
    add(createButton, gbc_createButton);
    
    JButton cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.insets = new Insets(0, 0, 5, 0);
    gbc_cancelButton.anchor = GridBagConstraints.WEST;
    gbc_cancelButton.gridx = 2;
    gbc_cancelButton.gridy = 2;
    add(cancelButton, gbc_cancelButton);
    
    cancelButton.addActionListener(new CloseActionListener(this));
 
  }
  
  @Override
  public String getTitle() {
    return "Oracle Connection" ;
  }

}
