package net.foxopen.clobber.clobberResource.fileSystemLocation;

import java.nio.file.Path;

import java.util.Date;

import nu.xom.Element;

/**
 * A file path.
 *
 * @author aled
 *
 */

public class FileLocation implements FileSystemLocation {

  private Path path;

  /**
   * Creates a FileLocation for the path supplied
   * 
   * @param path
   *          The path required
   */
  public FileLocation(Path path) {
    this.path = path;
  }

  @Override
  public Path getPath() {
    return this.path.getParent();
  }

  @Override
  public boolean isWriteable(Path pathToCheck) {

    boolean result = false;

    if (this.matchesPath(pathToCheck)) {
      result = this.path.toFile().canWrite();
    }
    return result;
  }

  @Override
  public boolean matchesPath(Path checkAgainst) {

    boolean result = checkAgainst.equals(this.path);
    return result;
  }

  @Override
  public String toString() {
    return this.path.getFileName().toString();
  }

  @Override
  public Date getLastModified() {
    return new Date(this.path.toFile().lastModified());
  }

@Override
public Element getElementRepresentation() {
	Element fileSystemElement = new Element("FILE_SYSTEM_LOCATION");
	Element filePath  = new Element("PATH");
	filePath.appendChild(this.path.toAbsolutePath().toString());
	fileSystemElement.appendChild(filePath);
	return fileSystemElement;
}
}
