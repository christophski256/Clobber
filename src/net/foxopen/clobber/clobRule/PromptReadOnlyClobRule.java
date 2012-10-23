package net.foxopen.clobber.clobRule;

import java.nio.file.Path;

public class PromptReadOnlyClobRule extends PromptClobRule implements ClobRule {

  public PromptReadOnlyClobRule(String prompt, ClobRuleProvider provider, ClobRule parent) {
    super(prompt, provider, parent);
  }

  @Override
  public boolean shouldPrompt(Path eventPath) {
    return super.getProvider().isResourceReadOnly(eventPath);
  }

}
