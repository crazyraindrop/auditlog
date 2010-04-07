package play.modules.auditlog;

import org.hibernate.event.*;
import play.db.jpa.JPASupport;

public class AuditLogListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    public void onPostInsert(PostInsertEvent event) {
        JPASupport entity = (JPASupport) event.getEntity();
        if (entity.getClass().isAnnotationPresent(Auditable.class)) {
            String model = entity.getClass().getName().replaceFirst("models.", "");
            String modelId = entity.getEntityId().toString();
            String[] properties = event.getPersister().getPropertyNames();
            Object[] values = event.getState();
            for (int i=0; i<properties.length; i++) {
                AuditLog.invoke(
                        "onCreate",
                        model,
                        modelId,
                        properties[i],
                        values[i] == null ? "NULL" : values[i].toString()
                );
            }
        }
    }

    public void onPostUpdate(PostUpdateEvent event) {
        JPASupport entity = (JPASupport) event.getEntity();
        if (entity.getClass().isAnnotationPresent(Auditable.class)) {
            String model = entity.getClass().getName().replaceFirst("models.", "");
            String modelId = entity.getEntityId().toString();
            String[] properties = event.getPersister().getPropertyNames();
            Object[] oldValues = event.getOldState();
            Object[] values = event.getState();
            for (int i=0; i<properties.length; i++) {
                boolean updated = false;
                if (oldValues[i] == null) {
                    if (values[i] != null) {
                        updated = true;
                    }
                } else if (!oldValues[i].equals(values[i])) {
                    updated = true;
                }
                if (updated) {
                    AuditLog.invoke(
                            "onUpdate",
                            model,
                            modelId,
                            properties[i],
                            oldValues[i] == null ? "NULL" : oldValues[i].toString(),
                            values[i] == null ? "NULL" : values[i].toString()
                    );
                }
            }
        }
    }

    public void onPostDelete(PostDeleteEvent event) {
        JPASupport entity = (JPASupport) event.getEntity();
        if (entity.getClass().isAnnotationPresent(Auditable.class)) {
            String model = entity.getClass().getName().replaceFirst("models.", "");
            String modelId = entity.getEntityId().toString();
            String[] properties = event.getPersister().getPropertyNames();
            Object[] values = event.getDeletedState();
            for (int i=0; i<properties.length; i++) {
                AuditLog.invoke(
                        "onDelete",
                        model,
                        modelId,
                        properties[i],
                        values[i] == null ? "NULL" : values[i].toString()
                );
            }
        }
    }

}
