package net.foxopen.clobber.exception;

public class ClobberInstanceInitialisationException extends Exception {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private static final String message = "Error Initialising Clobber instance.";

  public ClobberInstanceInitialisationException(Exception e) {
    super(message, e);
  }

}
