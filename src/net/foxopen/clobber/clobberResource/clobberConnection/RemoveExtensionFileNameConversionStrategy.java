package net.foxopen.clobber.clobberResource.clobberConnection;

/**
 * Removes the extension from a string.
 * 
 * @author aled
 * 
 */
public class RemoveExtensionFileNameConversionStrategy implements FileNameConversionStrategy {

  @Override
  public String getConvertedFileName(String fileName) {
    return fileName.replaceFirst("\\.[^\\.]+$", "");
  }

}
