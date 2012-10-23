package net.foxopen.clobber;

import java.io.File;
import java.util.List;

import net.foxopen.clobber.clobberResource.ClobberResource;

public interface ClobberProject {

  void startListening();

  void stopListening();

  String getFileLocation();

  boolean hasChanged();

  List<ClobberResource> getResources();

  String getName();

  void save();

  void addResources(File[] files);

}
