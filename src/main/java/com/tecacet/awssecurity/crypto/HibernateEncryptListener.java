package com.tecacet.awssecurity.crypto;

import com.tecacet.awssecurity.service.EncryptionService;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.event.spi.AbstractPreDatabaseOperationEvent;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.springframework.stereotype.Component;

import jodd.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class HibernateEncryptListener implements PreInsertEventListener, PreUpdateEventListener {

    private final BeanUtil beanUtil = BeanUtil.declaredForcedSilent;

    private final EncryptionService encryptionService;

    private final AnnotatedFieldProvider annotatedFieldProvider;

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        log.debug("ENCRYPT on pre-insert");
        Object[] state = event.getState();
        return encrypt(event, state);
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        log.debug("ENCRYPT on pre-update");
        Object[] state = event.getState();
        return encrypt(event, state);
    }

    private boolean encrypt(AbstractPreDatabaseOperationEvent event, Object[] state) {
        Object entity = event.getEntity();
        String[] propertyNames = event.getPersister().getEntityMetamodel().getPropertyNames();
        List<Field> fields = annotatedFieldProvider.getAnnotatedFields(entity.getClass(), Encrypted.class);
        for (Field field : fields) {
            byte[] data = beanUtil.getProperty(entity, field.getName());
            byte[] encrypted = encryptionService.encrypt(data);
            setValue(state, propertyNames, field.getName(), encrypted, entity);
        }
        return false;
    }

    void setValue(Object[] currentState, String[] propertyNames, String propertyToSet, Object value, Object entity) {
        int index = ArrayUtils.indexOf(propertyNames, propertyToSet);
        if (index >= 0) {
            currentState[index] = value;
        } else {
            log.error("Field {} not found on entity {}.", propertyToSet, entity.getClass().getName());
        }
    }
}
