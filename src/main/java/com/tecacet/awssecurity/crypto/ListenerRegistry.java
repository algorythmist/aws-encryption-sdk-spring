package com.tecacet.awssecurity.crypto;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Component
@RequiredArgsConstructor
public class ListenerRegistry {

    private final EntityManagerFactory entityManagerFactory;
    private final HibernateEncryptListener encryptListener;
    private final HibernateDecryptListener decryptListener;

    @PostConstruct
    private void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(encryptListener);
        registry.getEventListenerGroup(EventType.POST_LOAD).appendListener(decryptListener);
    }
}
