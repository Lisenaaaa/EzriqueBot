package xyz.deftu.ezrique.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.List;

public class PermissionHelper {

    public static boolean hasPermissions(Member member, Permission... permissions) {
        return member != null && member.hasPermission(permissions);
    }

    public static String getInvalidPermissionsMessage(Permission... permissions) {
        List<Permission> perms = Arrays.asList(permissions);
        String value = "Only members with the %s permission can use this command.";

        StringBuilder replacement = new StringBuilder();
        for (Permission permission : perms) {
            int index = perms.indexOf(permission);
            boolean first = index == 0;
            boolean last = index == perms.size() - 1;
            if (!first && !last)
                replacement.append(", ");
            if (!first && last)
                replacement.append("and ");
            replacement.append("`").append(permission.getName()).append("`");
        }

        return String.format(value, replacement);
    }

}