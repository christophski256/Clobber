package net.foxopen.clobber.clobRule;

import java.nio.file.Path;

public class ReadOnlyClobRule implements ClobRule {

  private ClobRuleProvider provider;
  private ClobRule parent;

  public ReadOnlyClobRule(ClobRuleProvider provider, ClobRule parent) {
    this.provider = provider;
    this.parent = parent;
    assert (this.provider != null && this.parent != null);
  }

  @Override
  public boolean shouldClob(Path path) {
    return !this.provider.isResourceReadOnly(path) && this.parent.shouldClob(path);
  }

}
