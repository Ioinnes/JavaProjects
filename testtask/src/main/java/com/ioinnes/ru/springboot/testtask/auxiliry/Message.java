package com.ioinnes.ru.springboot.testtask.auxiliry;

public class Message {
    // universal
    public static final String THERE_IS_NO_USER_WITH_ID
            = "User with id = %d doesn't exist.";
    public static final String SOMETHING_BAD_HAPPENED_WITH_DATABASE
            = "Something bad happened with database.";
    public static final String INVALID_DATA = "Invalid data.";
    public static final String INVALID_STATUS = "Invalid status";

    // get request messages
    public static final String GET_BY_ID_REQUEST_WILL_BE_EXECUTED_NOW
            = "GET by id = %d request will be executed now.";

    public static final String USER_WITH_ID_WAS_FOUND_AND_RETURNED
            = "%s was found and returned.";

    // delete request messages
    public static final String DELETE_BY_ID_REQUEST_WILL_BE_EXECUTED_NOW
            = "DELETE by id = %d request will be executed now.";

    public static final String USER_WITH_ID_WAS_FOUND_AND_REMOVED
            = "User with id = %d was found and removed.";

    //post reques
    public static final String POST_USER_WILL_BE_EXECUTED_NOW
            = "POST %s will be executed now.";

    public static final String TRY_TO_ADD_USER_WITH_INVALID_USERNAME_OR_EMAIL
            = "Try to add user with invalid username or email.";

    public static final String THERE_IS_USER_WITH_THE_SAME_USERNAME_OR_EMAIL
            = "There is user with the same username or email.";

    public static final String TRY_TO_ADD_USER_WITH_INVALID_IMAGE
            = "Try to add user with invalid image.";

    public static final String USER_WITH_ID_WAS_ADDED
            = "User with id = %d was added.";

    // get with conditions request
    public static final String TRY_TO_EXECUTE_REQUEST_WITH_WRONG_STATUS_PARAMETER
            = "Try to execute request with wrong status parameter.";

    public static final String GET_WITH_CONDITIONS_STATUS_TIMESTAMP_WILL_BE_EXECUTED_NOW
            = "GET WITH CONDITIONS: status = %s,  timestamp = %s will be executed now.";

    public static final String GET_WITH_CONDITIONS_CONDITIONS_STATUS_S_TIMESTAMP_S_WAS_EXECUTED_SUCCESSFULLY
            = "GET WITH CONDITIONS: status = %s,  timestamp = %s was executed successfully.";
    public static final String INVALID_IMAGE = "Invalid image.";

    // put request
    public static final String SOMETHING_WRONG_WITH_ANOTHER_SERVER
            = "Something wrong with another server/api.";

    public static final String PUT_REQUEST_WITH_PARAMETERS_ID_D_STATUS_S_WILL_BE_EXECUTED_NOW
            = "PUT request with parameters: id = %d, status = %s will be executed now.";

    public static final String USER_WITH_ID_CHANGED_HIS_STATUS_FROM_S_TO_S
            = "User with id = %d changed his status from %s to %s.";

    // get all

    public static final String GET_ALL_REQUEST_WILL_BE_EXECUTED_NOW
            = "GET ALL request will be executed now.";

    public static final String GET_ALL_REQUEST_WAS_EXECUTED_SUCCESSFULLY
            = "GET ALL request was executed successfully";
}
