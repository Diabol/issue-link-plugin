package org.jenkinsci.plugins.issuelink;


import hudson.model.FreeStyleProject;
import hudson.util.FormValidation;
import org.junit.Test;

import static org.junit.Assert.*;

public class IssueLinkJobPropertyTest {

    @Test
    public void testDoLinkCheck() {
        assertEquals(FormValidation.Kind.ERROR, IssueLinkJobProperty.DESCRIPTOR.doLinkCheck(null).kind);
        assertEquals(FormValidation.Kind.OK, IssueLinkJobProperty.DESCRIPTOR.doLinkCheck("http://issue/{0}").kind);
    }

    @Test
    public void testDoRegexpCheck() {
        assertEquals(FormValidation.Kind.ERROR, IssueLinkJobProperty.DESCRIPTOR.doRegexCheck(null).kind);
        assertEquals(FormValidation.Kind.ERROR, IssueLinkJobProperty.DESCRIPTOR.doRegexCheck("*").kind);
        assertEquals(FormValidation.Kind.ERROR, IssueLinkJobProperty.DESCRIPTOR.doRegexCheck(":*").kind);
        assertEquals(FormValidation.Kind.OK, IssueLinkJobProperty.DESCRIPTOR.doRegexCheck("(.*)").kind);
    }

    @Test
    public void testIsApplicable() {
        assertFalse(IssueLinkJobProperty.DESCRIPTOR.isApplicable(FreeStyleProject.class));
    }
}
