package net.foxopen.clobber.clobRule;

import java.nio.file.Path;

/**
 * A provider of information to help decide whether a ClobRule returns true.
 * 
 * @author aled
 * 
 */
public interface ClobRuleProvider {



  /**
   * Returns whether the version of the resource, relevant to the path supplied,
   * on the Remote end of the clob is the same as the last clobbed version.
   * 
   * @param path
   *          The path to check.
   * @return Whether the version at the remote end is the same as the last
   *         clobbed version
   */
  boolean isPreviousClobbedResourceDifferentFromCurrentRemoteResource(Path path);

  /**
   * Is the local version of the resource read-only?
   * 
   * @param path
   *          Path to check.
   * @return Whether the resource is read only.
   */
  boolean isResourceReadOnly(Path path);

}
