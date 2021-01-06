package com.epam.esm.controller.command;

import com.epam.esm.entity.RoleType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Permissive action rights
 */
public class ActionPermission {
    private static Map<String, RoleType> actionPermissionMap = new HashMap<>();
    private static ActionPermission instance;

    private ActionPermission() {
    }

    /**
     * Constructor
     *
     * @return action permission
     */
    public static ActionPermission getInstance() {
        if (instance == null) {
            instance = new ActionPermission();
            fillPermission();
        }
        return instance;
    }

    /**
     * Obtaining all permission
     *
     * @return Map all permission
     */
    public Map<String, RoleType> getActionPermissionMap() {
        return Collections.unmodifiableMap((actionPermissionMap));
    }

    private static void fillPermission() {

        actionPermissionMap.put(ActionType.ADMIN.name(), RoleType.ADMIN);
        actionPermissionMap.put(ActionType.EDIT_USERS.name(), RoleType.ADMIN);
        actionPermissionMap.put(ActionType.CREATE_USER.name(), RoleType.ADMIN);
        actionPermissionMap.put(ActionType.UPDATE_USER.name(), RoleType.ADMIN);
    }
}
