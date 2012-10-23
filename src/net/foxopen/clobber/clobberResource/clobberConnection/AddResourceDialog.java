package net.foxopen.clobber.clobberResource.clobberConnection;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import net.foxopen.clobber.Clobber;

public class AddResourceDialog extends JDialog {
  private JTabbedPane connectionTypeTabPane;
  private List<AddResourcePanel> panels;
 

  /**
   * Create the dialog.
   * @param clobberFrame 
   */
  private AddResourceDialog(JFrame clobberFrame, List<AddResourcePanel> panels, String resourceName) {
    super(clobberFrame,true);
    setTitle("Add Resource");
    setBounds(100, 100, 450, 300);
    getContentPane().setLayout(new BorderLayout());
    {
      connectionTypeTabPane = new JTabbedPane(JTabbedPane.TOP);
      getContentPane().add(connectionTypeTabPane, BorderLayout.NORTH);
      {
        for(AddResourcePanel p : panels ){
          
          connectionTypeTabPane.addTab(p.getTitle(), null, p , null);
        }
        
        
      }
    }
  }
  
  public static AddResourceDialog getDefaultAddResourceDialog(String resourceName){
    List<AddResourcePanel> connectionTypePanels= ClobberConnection.getAllAddResourcePanels();  
    return new AddResourceDialog(Clobber.clobberFrame, connectionTypePanels, resourceName);
  }

}
