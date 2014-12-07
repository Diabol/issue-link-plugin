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
        new IssueLinkChangeLogAnnotator().annotate(null, null, text);
        assertEquals("<a href='http://issues.nowhere/DM-5674'>DM-5674</a> Fixed bug", text.toString(false));
    }

}
