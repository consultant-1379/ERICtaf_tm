/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.infrastructure.templating;

import com.ericsson.cifwk.tm.domain.events.UserAssignedEvent;
import com.ericsson.cifwk.tm.integration.email.EmailService;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.netflix.governator.annotations.WarmUp;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static java.lang.String.format;

@Singleton
public class TemplatingService {

    static final String NEW_ASSIGNMENT = "new_assignment.ftl";

    private final Logger logger = LoggerFactory.getLogger(TemplatingService.class);

    @Inject
    private EmailService emailService;

    @Inject
    private Provider<UserDirectory> userManagement;

    @Inject
    private EventBus eventBus;

    private Configuration cfg;

    @WarmUp
    void configure() {
        eventBus.register(this);

        cfg = new Configuration();

        cfg.setClassForTemplateLoading(TemplatingService.class, "..\\..\\..\\..\\..\\..\\templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLocalizedLookup(false);
    }

    @Subscribe
    public void onEvent(UserAssignedEvent e) {
        UserInfo userInfo = userManagement.get().findByUsernameOrEmail(e.getUserId());
        Map<String, Object> variables = Maps.newHashMap();
        String hostName = e.getHostName();

        variables.put("url", format("%s/#tm/viewTestCampaign/%s", hostName, e.getTestPlanId()));
        variables.put("user", userInfo);
        variables.put("testCases", e.getTestCases());

        String text = generateFromTemplate(NEW_ASSIGNMENT, variables);
        emailService.sendEmail(userInfo.getEmail(), "Please review Test Case(s)", text);
    }

    public String generateFromTemplate(String templateName, Map<String, Object> variables) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Template template = cfg.getTemplate(templateName);
            template.process(variables, new PrintWriter(out));

            return out.toString();
        } catch (IOException | TemplateException e) {
            logger.error("Exception while processing template " + templateName, e);
        }

        return null;
    }
}
