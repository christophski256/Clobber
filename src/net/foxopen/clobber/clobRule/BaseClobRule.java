package net.foxopen.clobber.clobRule;

import java.nio.file.Path;

/**
 * Base Clobber rule returns true
 * 
 * @author aled
 * 
 */
public class BaseClobRule implements ClobRule {

  @Override
  public boolean shouldClob(Path path) {
    return true;
  }

}
