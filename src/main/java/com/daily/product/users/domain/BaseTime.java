package com.daily.product.users.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
//BaseTime 추상 클래스를 상속하는 클래스는 JPA가 생성일자, 수정일자 컬럼을 인식하게 됩니다.
@MappedSuperclass
//해당 클래스에 Auditing 기능을 포함
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime {
    @CreatedDate
    private LocalDateTime registerTime;
    public static String toStringDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
            .map(formatter::format)
            .orElse("");
    }
}