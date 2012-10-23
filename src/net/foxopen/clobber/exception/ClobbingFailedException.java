package net.foxopen.clobber.exception;

public class ClobbingFailedException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static final String msg = "Clobbing Failed";

  public ClobbingFailedException(Exception e) {
    super(msg, e);
  }

}
