package com.tickshow.backend.transport.templates;

import com.tickshow.backend.exception.ContentCreationException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageResponseTemplate implements NotificationContent {
    private static final Logger log = LoggerFactory.getLogger(PasswordResetTemplate.class);

    private final Configuration configuration;

    @Autowired
    public MessageResponseTemplate(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getContent(Object... object) throws ContentCreationException {
        try {
            String name = (String) object[0];
            String reply = (String) object[1];
            return FreeMarkerTemplateUtils.processTemplateIntoString(getTemplate(), getModel(name, reply));
        } catch (IOException | TemplateException e) {
            log.error("An error occurred while constructing freemarker template.", e);
            throw new ContentCreationException("Failed to construct freemarker template");
        }
    }

    private Template getTemplate() throws IOException {
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");
        return configuration.getTemplate("MessageResponse.ftl");
    }

    private Map<String, Object> getModel(String name, String reply) {
        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        model.put("reply", reply);
        return model;
    }
}