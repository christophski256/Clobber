package net.foxopen.clobber.clobberResource.fileSystemLocation;

import java.nio.file.Path;

import java.util.Date;

import nu.xom.Element;

/**
 * An interface to describe the state of a file system location.
 *
 * @author aled
 *
 */
public interface FileSystemLocation {

  /**
   * Returns a Path being used by the object. Can either point to a directory or
   * a file.
   * 
   * @return the Path stored used in the filesystem location
   */
  public Path getPath();

  /**
   * 
   * @param pathToCheck
   *          Check whether the file path supplied is writable
   * @return Whether the file is writable
   */
  public boolean isWriteable(Path pathToCheck);

  /**
   * Checks the given Path against the path in the object. If either the path
   * matches exactly, the path is a direct child of the object's path, or the
   * path is a descendant of the object's path and the instance of
   * FileSystemLocation is recursive returns true then {@code true} is returned.
   * 
   * @param checkAgainst
   *          The {@link Path} being checked
   * @return Whether the {@link Path} matches.
   */
  public  boolean matchesPath(Path checkAgainst);
  
 /**
   *Returns the date that the file location was last modified
   * @return The date the file location was last modified
   */
  public Date getLastModified();

public Element getElementRepresentation();

}
