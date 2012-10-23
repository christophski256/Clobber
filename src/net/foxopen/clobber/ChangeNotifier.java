package net.foxopen.clobber;

public interface ChangeNotifier {
  
  public void registerListener(ChangeListener projectToNotify);
  public void removeListener(ChangeListener projectToNotify);
  public void notifyListeners();
}
