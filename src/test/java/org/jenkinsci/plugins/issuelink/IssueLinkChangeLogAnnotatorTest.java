package org.jenkinsci.plugins.issuelink;

import hudson.MarkupText;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertEquals;

public class IssueLinkChangeLogAnnotatorTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();


    @Test
    public void testNoIssue() throws Exception {
        MarkupText text = new MarkupText("Fixed bug");
        new IssueLinkChangeLogAnnotator().annotate(null, null, text);
        assertEquals("Fixed bug", text.toString(false));
    }

    @Test
    public void testHasOneIssue() throws Exception {
        MarkupText text = new MarkupText("DM-5674 Fixed bug");
        IssueLinkJobProperty.DESCRIPTOR.setRegex("([a-zA-Z][a-zA-Z]+-\\d+)");
        IssueLinkJobProperty.DESCRIPTOR.setLink("http://issues.nowhere/{0}");
        new IssueLinkChangeLogAnnotator().annotate(null, null, text);
        assertEquals("<a href='http://issues.nowhere/DM-5674'>DM-5674</a> Fixed bug", text.toString(false));
    }

    @Test
    public void testHasTwo() throws Exception {
        MarkupText text = new MarkupText("DM-5674 Fixed bug DM-3423 Fixed this too");
        IssueLinkJobProperty.DESCRIPTOR.setRegex("([a-zA-Z][a-zA-Z]+-\\d+)");
        IssueLinkJobProperty.DESCRIPTOR.setLink("http://issues.nowhere/{0}");
        new IssueLinkChangeLogAnnotator().annotate(null, null, text);
        assertEquals("<a href='http://issues.nowhere/DM-5674'>DM-5674</a> Fixed bug <a href='http://issues.nowhere/DM-3423'>DM-3423</a> Fixed this too", text.toString(false));
    }

    @Test
    public void testHasMultiple() throws Exception {
        MarkupText text = new MarkupText("DMZ-5674 Fixed bug DM-3423 Fixed this too");
        IssueLinkJobProperty.DESCRIPTOR.setRegex("([a-zA-Z][a-zA-Z]+-\\d+)|([a-zA-Z][a-zA-Z][a-zA-Z]+-\\d+)");
        IssueLinkJobProperty.DESCRIPTOR.setLink("http://issues.nowhere/{0}");
        new IssueLinkChangeLogAnnotator().annotate(null, null, text);
        assertEquals("<a href='http://issues.nowhere/DMZ-5674'>DMZ-5674</a> Fixed bug <a href='http://issues.nowhere/DM-3423'>DM-3423</a> Fixed this too", text.toString(false));
    }

}
