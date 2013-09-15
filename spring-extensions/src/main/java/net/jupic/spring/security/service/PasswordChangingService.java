package net.jupic.spring.security.service;

/**
 * @author chang jung pil
 *
 */
public interface PasswordChangingService {
    void changPassword(String principal, String credential);
}
