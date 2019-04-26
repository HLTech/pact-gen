package com.hltech.pact.gen.testfeignclient

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

class DateTimeDto {

    LocalDate localDate
    LocalDateTime localDateTime
    ZonedDateTime zonedDateTime

    DateTimeDto(LocalDate localDate, LocalDateTime localDateTime, ZonedDateTime zonedDateTime) {
        this.localDate = localDate
        this.localDateTime = localDateTime
        this.zonedDateTime = zonedDateTime
    }

    LocalDate getLocalDate() {
        return localDate
    }

    LocalDateTime getLocalDateTime() {
        return localDateTime
    }

    ZonedDateTime getZonedDateTime() {
        return zonedDateTime
    }
}
