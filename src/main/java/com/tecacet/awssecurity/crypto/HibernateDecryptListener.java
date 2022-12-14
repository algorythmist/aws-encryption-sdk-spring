package com.tecacet.awssecurity.crypto;

import com.tecacet.awssecurity.service.EncryptionService;

import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.springframework.stereotype.Component;

import jodd.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HibernateDecryptListener implements PostLoadEventListener {

    private final BeanUtil beanUtil = BeanUtil.declaredForcedSilent;

    private final EncryptionService encryptionService;

    private final AnnotatedFieldProvider annotatedFieldProvider;

    @Override
    public void onPostLoad(PostLoadEvent event) {
        log.info("DECRYPT on post-load");
        Object entity = event.getEntity();
        List<Field> fields = annotatedFieldProvider.getAnnotatedFields(entity.getClass(), Encrypted.class);
        for (Field field : fields) {
            byte[] data = beanUtil.getProperty(entity, field.getName());
            byte[] decrypted = encryptionService.decrypt(data);
            beanUtil.setProperty(entity, field.getName(), decrypted);
        }
    }
}
