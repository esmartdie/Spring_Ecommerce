package com.ecommerce.ecommerce.utils;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionUtilsTest {
    private SessionUtils sessionUtils;

    @Mock
    private HttpSession mockSession;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionUtils = new SessionUtils();
    }

    @Test
    public void testGetUserIdFromSession_WithUserIdInSession() {
        // Arrange
        int expectedUserId = 123;
        when(mockSession.getAttribute("userId")).thenReturn(expectedUserId);

        // Act
        Integer actualUserId = sessionUtils.getUserIdFromSession(mockSession);

        // Assert
        assertEquals(expectedUserId, actualUserId);
    }

    @Test
    public void testGetUserIdFromSession_WithoutUserIdInSession() {
        when(mockSession.getAttribute("userId")).thenReturn(null);

        Integer actualUserId = sessionUtils.getUserIdFromSession(mockSession);

        assertNull(actualUserId);
    }

    @Test
    public void testRemoveAllAttributes() {
        Enumeration<String> attributeNames = Collections.enumeration(Arrays.asList("attr1", "attr2"));
        when(mockSession.getAttributeNames()).thenReturn(attributeNames);

        sessionUtils.removeAllAttributes(mockSession);

        verify(mockSession, atLeastOnce()).getAttributeNames();
        verify(mockSession, atLeastOnce()).removeAttribute(anyString());
    }

}