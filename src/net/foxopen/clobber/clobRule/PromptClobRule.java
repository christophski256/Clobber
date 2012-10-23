package net.foxopen.clobber.clobRule;

import java.nio.file.Path;

import net.foxopen.clobber.prompts.Prompter;

/**
 * Abstract class to prompt on failure of a condition if the user wants to carry
 * on.
 * 
 * @author aled
 */
public abstract class PromptClobRule implements ClobRule {

  private String prompt;
  private ClobRuleProvider provider;
  private ClobRule parent;

  protected PromptClobRule(String prompt, ClobRuleProvider provider, ClobRule parent) {
    this.prompt = prompt;
    this.provider = provider;
    this.parent = parent;
    assert (this.parent != null && this.prompt != null && this.provider != null);
  }

  /**
   * Returns the provider supplied to the clob rule.
   * 
   * @return The provider for the rule.
   */
  protected ClobRuleProvider getProvider() {
    return this.provider;
  }

  @Override
  public final boolean shouldClob(Path eventPath) {
    if (this.shouldPrompt(eventPath)) {
      return Prompter.getPrompter().getBoolean(this.prompt);
    } else {
      return this.parent.shouldClob(eventPath);
    }
  }

  /**
   * Method to describe whether a prompt should be raised.
   * 
   * @param eventPath
   *          The path being checked.
   * @return Whether a prompt should be raised.
   */
  protected abstract boolean shouldPrompt(Path eventPath);

}
