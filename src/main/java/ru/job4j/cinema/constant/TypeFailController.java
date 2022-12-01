package ru.job4j.cinema.constant;

/**
 * Тип, от которого зависит страница, открывающаяся
 * при возникновении ошибки
 * @author Burlakov
 */
public class TypeFailController {
    /**
     * пользователь
     */
    public static final int USER = 1;
    /**
     * страна
     */
    public static final int COUNTRY = 2;
    /**
     * фильм
     */
    public static final int MOVIE = 3;
    /**
     * сеанс
     */
    public static final int SESSION = 4;
    /**
     * билет
     */
    public static final int TICKET = 5;

}
