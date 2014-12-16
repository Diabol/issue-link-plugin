package org.jenkinsci.plugins.issuelink;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.text.MessageFormat;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class IssueLinkJobProperty extends JobProperty<AbstractProject<?, ?>> {

    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public static final class DescriptorImpl extends JobPropertyDescriptor {
        private String regex;
        private String link;

        public DescriptorImpl() {
            super(IssueLinkJobProperty.class);
            load();
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return false;
        }

        public String getDisplayName() {
            return "Issue Link";
        }

        @Override
        public IssueLinkJobProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            return new IssueLinkJobProperty();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) {
            regex = req.getParameter("issuelink.regex");
            link = req.getParameter("issuelink.link");

            save();
            return true;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }

        public String getRegex() {
            if (regex == null) return "([a-zA-Z][a-zA-Z]+-\\d+)";
            return regex;
        }

        public String getLink() {
            if (link == null) return "http://issues.nowhere/{0}";
            return link;
        }

        public FormValidation doRegexCheck(@QueryParameter String value) {
            if (Util.fixEmpty(value) == null) {
                return FormValidation.error("No Issue ID regex");
            }
            try {
                Pattern pattern = Pattern.compile(value);
                if (pattern.matcher("").groupCount() == 1) {
                    return FormValidation.ok();
                } else {
                    return FormValidation.error("No capture group defined");
                }
            } catch (PatternSyntaxException e) {
                return FormValidation.error(e, "Syntax error in regular-expression pattern");
            }
        }

        public FormValidation doLinkCheck(@QueryParameter String value) {
            if (Util.fixEmpty(value) == null) {
                return FormValidation.error("No link defined");
            }
            try {
                MessageFormat.format(value, "issue");
            } catch (IllegalArgumentException e) {
                return FormValidation.error("Invalid url: " + e.getMessage());
            }
            return FormValidation.ok();
        }
    }

}
