package play.modules.auditlog;

import play.Logger;
import play.Play;
import play.utils.Java;

import java.util.List;

public class AuditLog {

    protected static Object invoke(String m, Object... args) {
        Class auditLog = null;
        List<Class> classes = Play.classloader.getAssignableClasses(AuditLog.class);
        if (classes.size() == 0) {
            Logger.error("AuditLog controller not found");
            return null;
        } else {
            auditLog = classes.get(0);
            for (Class clazz : classes) {
                if (!clazz.getName().contains("DefaultAuditLogEvents")) {
                    auditLog = clazz;
                }
            }
        }
        try {
            return Java.invokeStaticOrParent(auditLog, m, args);
        } catch (Exception e) {
            Logger.error(e, "Unable to invoke AuditLog method");
            return null;
        }
    }

}
