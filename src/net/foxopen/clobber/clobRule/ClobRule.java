package net.foxopen.clobber.clobRule;

import java.nio.file.Path;

/**
 * Interface for a clob rule describing whether a clob should be performed.
 * 
 * @author aled
 * 
 */
public interface ClobRule {
  /**
   * Returns whether the clob should be performed
   * 
   * @param eventPath
   *          The path to check
   * @return Whether the clob should be performed.
   */
  public boolean shouldClob(Path eventPath);
}
