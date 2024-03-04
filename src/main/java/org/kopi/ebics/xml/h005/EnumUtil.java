package org.kopi.ebics.xml.h005;

import org.kopi.ebics.schema.h005.ContainerFlagType;
import org.kopi.ebics.schema.h005.UserPermissionType;
import org.kopi.ebics.enumeration.h005.ContainerType;
import org.kopi.ebics.enumeration.h005.AuthorisationLevel;

public class EnumUtil {

    public static AuthorisationLevel toAuthLevel(UserPermissionType alt) {
        if (alt == null || alt.getAuthorisationLevel() == null) return null;
        else return AuthorisationLevel.valueOf(alt.getAuthorisationLevel().toString());
    }

    public static ContainerType toContainerType(ContainerFlagType ctf) {
        if (ctf == null || ctf.getContainerType() == null) return null;
        else return ContainerType.valueOf(ctf.getContainerType().toString());
    }
}
