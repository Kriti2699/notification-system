package com.notification.emailworker.repository;

import com.notification.emailworker.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailStatsRepository extends JpaRepository<EmailLog,String>
{

}

