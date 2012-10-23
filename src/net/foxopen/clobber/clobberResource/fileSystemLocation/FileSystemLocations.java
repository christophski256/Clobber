package net.foxopen.clobber.clobberResource.fileSystemLocation;

import java.nio.file.FileSystems;

import nu.xom.Node;

/**
 * Contains factory methods that create FileSystemLocation objects.
 * 
 * @author fiviumuser
 * 
 */
public class FileSystemLocations {

  /**
   * Creates a FileSystemObject from a Fragment of XML
   * 
   * @param node
   *          The XML representing a file
   * @return The FileSystemLocation specified.
   */
  public static FileSystemLocation createFileSystemLocation(Node node) {

    FileSystemLocation fsl = null;

    if (node.query("/*/DIRECTORY").size() > 0) {
      throw new RuntimeException("Directories not implemented yet");
    } else {
      fsl = new FileLocation(FileSystems.getDefault().getPath(node.query("PATH").get(0).getValue()));
    }
    return fsl;
  }

}
