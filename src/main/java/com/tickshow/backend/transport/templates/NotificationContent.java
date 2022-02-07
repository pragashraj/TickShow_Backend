package com.tickshow.backend.transport.templates;

import com.tickshow.backend.exception.ContentCreationException;

public interface NotificationContent {
    String getContent(Object... object) throws ContentCreationException;
}