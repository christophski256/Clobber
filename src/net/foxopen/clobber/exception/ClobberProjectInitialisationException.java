package net.foxopen.clobber.exception;

public class ClobberProjectInitialisationException extends Exception {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  private static final String message = "Error Initialising Clobber project.";

  public ClobberProjectInitialisationException(Exception e) {
    super(message, e);
  }

}
