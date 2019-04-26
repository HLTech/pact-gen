package com.hltech.pact.gen.testfeignclient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class DateTimeDto {

    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private ZonedDateTime zonedDateTime;

    public DateTimeDto(LocalDate localDate, LocalDateTime localDateTime, ZonedDateTime zonedDateTime) {
        this.localDate = localDate;
        this.localDateTime = localDateTime;
        this.zonedDateTime = zonedDateTime;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }
}
