package net.foxopen.clobber.clobRule;

import java.nio.file.Path;

/**
 * Class to prompt the user if the the current version on the remote datastore
 * matches the previously clobbed version.
 * 
 * @author aled
 * 
 */
public class PromptDifferentClobRule extends PromptClobRule implements ClobRule {

  public PromptDifferentClobRule(String prompt, ClobRuleProvider provider, ClobRule parent) {
    super(prompt, provider, parent);

  }

  @Override
  public boolean shouldPrompt(Path eventPath) {
    return super.getProvider().isPreviousClobbedResourceDifferentFromCurrentRemoteResource(eventPath);
  }

}
