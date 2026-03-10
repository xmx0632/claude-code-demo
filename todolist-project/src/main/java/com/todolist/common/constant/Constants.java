package com.todolist.common.constant;

/**
 * Common Constants
 *
 * @author TodoList Team
 * @since 1.0.0
 */
public class Constants {

    /**
     * UTF-8 Encoding
     */
    public static final String UTF8 = "UTF-8";

    /**
     * Default page number
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * Default page size
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * Maximum page size
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * JWT Token header
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * JWT Token prefix
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Token type in claims
     */
    public static final String TOKEN_TYPE = "type";

    /**
     * Refresh token type
     */
    public static final String REFRESH_TOKEN = "refresh";

    /**
     * Login user key in redis
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * Token expire time in seconds (24 hours)
     */
    public static final long TOKEN_EXPIRE_SECONDS = 86400;

    /**
     * Password regex: must contain letters and numbers
     */
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).+$";

    /**
     * Username regex: only letters, numbers, underscore
     */
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_]+$";

    /**
     * Default password
     */
    public static final String DEFAULT_PASSWORD = "admin123";
}
