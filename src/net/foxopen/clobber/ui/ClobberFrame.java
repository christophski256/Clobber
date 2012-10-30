package net.foxopen.clobber.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import net.foxopen.clobber.ClobberModel;
import net.foxopen.clobber.ClobberProject;
import net.foxopen.clobber.ui.projectTable.ProjectPanel;

import org.apache.log4j.Logger;

/**
 * The main application window
 *
 * @author Aled Lewis
 *
 */
public class ClobberFrame extends JFrame {

  /**
   * Whatever
   */
  private static final long serialVersionUID = 1L;
  private JPanel contentPane;
  private JTabbedPane tabbedPane;
  private List<ProjectPanel> projectPanels;
  private Logger logger;

  /**
   * Create the clobber main application window.
   * 
   * @param cm
   *          The clobber model
   */
  public ClobberFrame(ClobberModel cm) {
    
    this.logger = Logger.getLogger(this.getClass());
    this.setTitle("Clobber");
    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    this.setBounds(100, 100, 450, 300);
    this.projectPanels = new ArrayList<ProjectPanel>();
    this.logger.debug("Creating clobber Window");
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      // Whatever
      this.logger.warn(e.getMessage());
    }
    
    JMenuBar menuBar = new JMenuBar();
    this.setJMenuBar(menuBar);

    JMenu mnProject = new JMenu("Project");
    menuBar.add(mnProject);

    JMenuItem mntmOpenProject = new JMenuItem("Open Project");
    mnProject.add(mntmOpenProject);
    
    mntmOpenProject.addActionListener(new AddProjectListener(this, cm));

    JMenuItem mntmCloseProject = new JMenuItem("Close Project");
    mnProject.add(mntmCloseProject);

    JMenu mnSettings = new JMenu("Settings");
    menuBar.add(mnSettings);
    this.contentPane = new JPanel();
    this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.setContentPane(this.contentPane);
    GridBagLayout gbl_contentPane = new GridBagLayout();
    gbl_contentPane.columnWidths = new int[] { 0, 0 };
    gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0 };
    gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
    gbl_contentPane.rowWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
    this.contentPane.setLayout(gbl_contentPane);

    this.tabbedPane = new JTabbedPane(SwingConstants.TOP);
    GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
    gbc_tabbedPane.gridheight = 3;
    gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
    gbc_tabbedPane.fill = GridBagConstraints.BOTH;
    gbc_tabbedPane.gridx = 0;
    gbc_tabbedPane.gridy = 0;
    this.contentPane.add(this.tabbedPane, gbc_tabbedPane);
    this.logger.debug("Adding project windows");
    for (ClobberProject cp : cm.getClobberInstance().getProjects()) {
      ProjectPanel pp = new ProjectPanel(cp);  
      this.addProjectToFrame(pp,cp.getName());
    }

  }
  
  public void  addProjectToFrame(ProjectPanel pp, String name){
    this.logger.debug("Adding project "+name+" to frame");
    this.projectPanels.add(pp);
    this.tabbedPane.addTab(name, pp);
  }
  
  public void removePanelFromFrame(ProjectPanel pp){
    this.logger.debug("Removing project "+pp+" to frame");
    this.projectPanels.remove(pp);
    this.tabbedPane.remove(pp);
  }

}
