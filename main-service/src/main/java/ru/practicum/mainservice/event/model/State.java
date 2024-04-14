package ru.practicum.mainservice.event.model;

public enum State {
    PENDING, //ожидает подтверждения
    PUBLISHED,//опубликовано
    CANCELED// отменено
}
