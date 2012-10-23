package net.foxopen.clobber.ui.projectTable;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.border.Border;

import net.foxopen.clobber.ClobberProject;
import net.foxopen.clobber.ClobberProjectImpl;
import net.foxopen.clobber.FileDrop;

public class ProjectPanel extends JPanel {

    private JScrollPane scrollPane;
    private ResourceTable projectTable;
    
    
    

    public ProjectPanel(ClobberProject cp) {
        this.setLayout(new BorderLayout());
        this.projectTable = new ResourceTable(cp.getResources());
        this.scrollPane = new JScrollPane();
        this.scrollPane.getViewport().add((this.projectTable));
        this.add(this.scrollPane,BorderLayout.CENTER);      
        new FileDrop(this,new ProjectFileListener(cp));
    }

}
