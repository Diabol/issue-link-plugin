package org.jenkinsci.plugins.issuelink;


import hudson.model.FreeStyleProject;
import hudson.util.FormValidation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.WithoutJenkins;
import org.kohsuke.stapler.StaplerRequest;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IssueLinkJobPropertyTest {
    @Rule
    public JenkinsRule jenkins = new JenkinsRule();


    @Test
    @WithoutJenkins
    public void testDoLinkCheck() {
        assertEquals(FormValidation.Kind.ERROR, IssueLinkJobProperty.DESCRIPTOR.doLinkCheck(null).kind);
        assertEquals(FormValidation.Kind.OK, IssueLinkJobProperty.DESCRIPTOR.doLinkCheck("http://issue/{0}").kind);
        assertEquals(FormValidation.Kind.ERROR, IssueLinkJobProperty.DESCRIPTOR.doLinkCheck("http://issue/{0,number,integer}").kind);
    }

    @Test
    @WithoutJenkins
    public void testDoRegexpCheck() {
        assertEquals(FormValidation.Kind.ERROR, IssueLinkJobProperty.DESCRIPTOR.doRegexCheck(null).kind);
        assertEquals(FormValidation.Kind.ERROR, IssueLinkJobProperty.DESCRIPTOR.doRegexCheck("*").kind);
        assertEquals(FormValidation.Kind.ERROR, IssueLinkJobProperty.DESCRIPTOR.doRegexCheck(":*").kind);
        assertEquals(FormValidation.Kind.OK, IssueLinkJobProperty.DESCRIPTOR.doRegexCheck("(.*)").kind);
    }

    @Test
    @WithoutJenkins
    public void testIsApplicable() {
        assertFalse(IssueLinkJobProperty.DESCRIPTOR.isApplicable(FreeStyleProject.class));
    }


    @Test
    public void testConfigure() {
        StaplerRequest request = Mockito.mock(StaplerRequest.class);
        when(request.getParameter("issuelink.regex")).thenReturn("([a-zA-Z][a-zA-Z][a-zA-Z]+-\\d+)");
        when(request.getParameter("issuelink.link")).thenReturn("http://issue/{0}");

        assertTrue(IssueLinkJobProperty.DESCRIPTOR.configure(request, null));
        assertEquals("http://issue/{0}", IssueLinkJobProperty.DESCRIPTOR.getLink());
        assertEquals("([a-zA-Z][a-zA-Z][a-zA-Z]+-\\d+)", IssueLinkJobProperty.DESCRIPTOR.getRegex());

    }

    @Test
    public void testDefaults() {
        IssueLinkJobProperty.DESCRIPTOR.setLink(null);
        IssueLinkJobProperty.DESCRIPTOR.setRegex(null);
        assertEquals("http://issues.nowhere/{0}", IssueLinkJobProperty.DESCRIPTOR.getLink());
        assertEquals("([a-zA-Z][a-zA-Z]+-\\d+)", IssueLinkJobProperty.DESCRIPTOR.getRegex());

    }


    @Test
    @WithoutJenkins
    public void testDisplayName() {
        assertEquals("Issue Link", IssueLinkJobProperty.DESCRIPTOR.getDisplayName());
    }

    @Test
    @WithoutJenkins
    public void testNewInstance() throws Exception {
        assertNotNull(IssueLinkJobProperty.DESCRIPTOR.newInstance(null, null));
    }

}
