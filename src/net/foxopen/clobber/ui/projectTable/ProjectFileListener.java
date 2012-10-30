package net.foxopen.clobber.ui.projectTable;

import java.io.File;

import net.foxopen.clobber.ClobberProject;
import net.foxopen.clobber.FileDrop;

import org.apache.log4j.Logger;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


public class ProjectFileListener implements FileDrop.Listener{
  private ClobberProject clobberProject;


  ProjectFileListener(ClobberProject cp) {
    this.clobberProject =cp;
  }

  @Override
  public void filesDropped(File[] files) {
    Logger l = Logger.getLogger(this.getClass());
    String loggingMessage = "Adding Files: ";
    for(int i = 0; i< files.length; i++){
      loggingMessage+=files[i].getName();
    }
    l.info(loggingMessage);
    this.clobberProject.addResources(files);
  }
}
