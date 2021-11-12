package xyz.deftu.ezrique.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class PermissionHelper {

    public static boolean hasPermissions(Member member, Permission... permissions) {
        return member != null && member.hasPermission(permissions);
    }

}