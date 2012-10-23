package net.foxopen.clobber.clobberResource.clobberConnection;

/**
 * Interface for processing a file name string
 * 
 * @author aled
 * 
 */
public interface FileNameConversionStrategy {
  /**
   * Process the file name.
   * 
   * @param fileName
   *          The file name.
   * @return Processed file name.
   */
  String getConvertedFileName(String fileName);

}
