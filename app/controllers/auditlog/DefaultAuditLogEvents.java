package controllers.auditlog;

import jobs.auditlog.SaveAuditLogEvent;
import models.auditlog.AuditLogEvent;
import play.modules.auditlog.AuditLog;
import play.mvc.Scope;

public class DefaultAuditLogEvents extends AuditLog {

    static String getActor() {
        return Scope.Session.current().get("username");
    }

    static void onCreate(String model, String modelId, String property, String value) {
        System.out.println("create");
        String actor = (String) invoke("getActor");
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
        System.out.println("update");
        String actor = (String) invoke("getActor");
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
        System.out.println("delete");
        String actor = (String) invoke("getActor");
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
