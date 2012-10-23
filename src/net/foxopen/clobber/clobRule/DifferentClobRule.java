package net.foxopen.clobber.clobRule;

import java.nio.file.Path;

/**
 * Clob rule to determine if the current version on the remote datastore matches
 * the previously clobbed version.
 * 
 * @author aled
 * 
 */
public class DifferentClobRule implements ClobRule {

  private ClobRuleProvider provider;
  private ClobRule parent;

  public DifferentClobRule(ClobRuleProvider provider, ClobRule parent) {
    this.provider = provider;
    this.parent = parent;
  }

  @Override
  public boolean shouldClob(Path path) {

    boolean different = this.provider.isPreviousClobbedResourceDifferentFromCurrentRemoteResource(path);

    return !different && this.parent.shouldClob(path);
  }

}
