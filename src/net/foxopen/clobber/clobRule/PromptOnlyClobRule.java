package net.foxopen.clobber.clobRule;

import java.nio.file.Path;

/**
 * Rule to prompt the user always
 * 
 * @author aled
 * 
 */
public class PromptOnlyClobRule extends PromptClobRule implements ClobRule {

  public PromptOnlyClobRule(String prompt, ClobRuleProvider provider, ClobRule parent) {
    super(prompt, provider, parent);
  }

  @Override
  public boolean shouldPrompt(Path eventPath) {
    // return false as we always want to clob
    return true;
  }

}
