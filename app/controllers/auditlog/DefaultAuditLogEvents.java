package controllers.auditlog;

import jobs.auditlog.SaveAuditLogEvent;
import models.auditlog.AuditLogEvent;
import play.modules.auditlog.AuditLog;
import play.mvc.Controller;

public class DefaultAuditLogEvents extends Controller {

    static String getActor() {
        return session.get("username");
    }

    static void onCreate(String model, String modelId, String property, String value) {
        String actor = (String) AuditLog.invoke("getActor");
        new SaveAuditLogEvent(
                model,
                modelId,
                AuditLogEvent.Operation.CREATE,
                property,
                null,
                value,
                actor
        ).now();
    }

    static void onUpdate(String model, String modelId, String property, String oldValue, String value) {
        String actor = (String) AuditLog.invoke("getActor");
        new SaveAuditLogEvent(
                model,
                modelId,
                AuditLogEvent.Operation.UPDATE,
                property,
                oldValue,
                value,
                actor
        ).now();
    }

    static void onDelete(String model, String modelId, String property, String value) {
        String actor = (String) AuditLog.invoke("getActor");
        new SaveAuditLogEvent(
                model,
                modelId,
                AuditLogEvent.Operation.DELETE,
                property,
                value,
                null,
                actor
        ).now();
    }

}
