package net.foxopen.clobber.clobberResource;

import java.util.List;

import nu.xom.Element;

public interface ResourceSerialiser {
  
  public Element getElementRepresentation(List<ClobberResource> resources);
  
}
