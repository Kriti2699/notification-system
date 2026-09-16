package com.notification.smsworkers.repository;

import com.notification.smsworkers.entity.SmsTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsRepository extends JpaRepository<SmsTemplate,String> {

    Optional<SmsTemplate> findByTemplateCode(String templateCode);

}
