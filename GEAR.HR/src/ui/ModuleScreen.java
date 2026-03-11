package ui;

import service.ApplicationContext;
import service.RoleGroup;

import javax.swing.JFrame;

/**
 * [INTERFACE] Contract for module screens that can be shown with a single polymorphic call.
 * Enables Main to open any screen without knowing the concrete class.
 */
public interface ModuleScreen {
    /**
     * [INTERFACE] Shows this module screen; implementations obtain services from ctx.
     * @param parentFrame parent window (for positioning)
     * @param userId current user id
     * @param role current user role
     * @param group role group for permissions
     * @param ctx application context (services, etc.)
     */
    void show(JFrame parentFrame, String userId, String role, RoleGroup group, ApplicationContext ctx);
}
