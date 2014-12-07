package org.jenkinsci.plugins.issuelink;

import hudson.Extension;
import hudson.MarkupText;
import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogAnnotator;
import hudson.scm.ChangeLogSet;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Extension
@SuppressWarnings("unused")
public class IssueLinkChangeLogAnnotator extends ChangeLogAnnotator {
    @Override
    public void annotate(AbstractBuild<?, ?> build, ChangeLogSet.Entry change, MarkupText text) {
        Pattern p = Pattern.compile(IssueLinkJobProperty.DESCRIPTOR.getRegex());
        Matcher m = p.matcher(text.getText());
        while (m.find()) {
            String url = MessageFormat.format(IssueLinkJobProperty.DESCRIPTOR.getLink(), m.group(1));
            text.addMarkup(m.start(1), m.end(1), "<a href='" + url + "'>", "</a>");
        }
    }
}
