package com.example.todolist.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

 class LocalDateTimeExtension {
     companion object {
         fun fromStringToDate(str: String): LocalDateTime {
             val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
             return LocalDateTime.parse(str, dtf)
         }


         fun now(): LocalDateTime {
             val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
             return LocalDateTime.parse(LocalDateTime.now().format(dtf), dtf)
         }
     }
 }
