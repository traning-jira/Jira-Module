package com.cmcglobal.jira.plugins.utilities;

import com.cmcglobal.jira.plugins.entity.Leave;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utilities {
//    @JiraImport
//    private final CustomFieldManager customFieldManager;
//    @JiraImport
//    private IssueTypeManager issueTypeManager;
//    @JiraImport
//    private final AvatarManager avatarManager;
//    private Utilities(CustomFieldManager customFieldManager, IssueTypeManager issueTypeManager, AvatarManager avatarManager) {
//
//        this.customFieldManager = customFieldManager;
//        this.issueTypeManager = issueTypeManager;
//        this.avatarManager = avatarManager;
//    }
    private Utilities(){}

    public static List<String> listLeaveAttribute() {
        return getMethods(Leave.class);
    }
    private static String addWhiteSpace(final String str) {
        return str.replaceAll("(.)([A-Z0-9])", "$1 $2");
    }
    private static List<String> getMethods(final Class clazz) {
        final List<String> listMethods = new ArrayList<>();
        final Method[] methods = clazz.getDeclaredMethods();
        Arrays.sort(methods, (Method o1, Method o2) -> {
            final Order or1 = o1.getAnnotation(Order.class);
            final Order or2 = o2.getAnnotation(Order.class);
            // nulls last
            if (or1 != null && or2 != null) {
                return or1.value() - or2.value();
            } else if (or1 != null) {
                return -1;
            } else if (or2 != null) {
                return 1;
            }
            return o1.getName().compareTo(o2.getName());
        });
        for (final Method method : methods) {
            if (method.getAnnotation(Order.class) != null) {
                final String attribute = addWhiteSpace(method.getName().substring(3));
                if (!listMethods.contains(attribute)) {
                    listMethods.add(attribute);
                }
            }
        }

        return listMethods;
    }

}
