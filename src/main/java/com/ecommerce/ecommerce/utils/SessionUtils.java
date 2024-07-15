package com.ecommerce.ecommerce.utils;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

@Component
public class SessionUtils {

    public Integer getUserIdFromSession(HttpSession session) {
        Object userId = session.getAttribute("userId");
        return userId != null ? Integer.parseInt(userId.toString()) : null;
    }

    public void removeAllAttributes(HttpSession session) {
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            session.removeAttribute(attributeName);
        }
    }
}
