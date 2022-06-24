package com.ioinnes.ru.springboot.testtask.aspects;

import com.ioinnes.ru.springboot.testtask.auxiliry.Message;
import com.ioinnes.ru.springboot.testtask.entity.User;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;


@Aspect
@Component
public class UserControllerAspect {
    private final Logger logger = Logger.getLogger(UserControllerAspect.class.getSimpleName());

    @Around("execution(* com.ioinnes.ru.springboot.testtask.processor.UserProcessor.processFindById(..))")
    private Object processGetByIdRequestLoggingAspect(ProceedingJoinPoint proceedingJoinPoint) {

        final int id = (int) proceedingJoinPoint.getArgs()[0];

        logger.log(Level.INFO, String.format(Message.GET_BY_ID_REQUEST_WILL_BE_EXECUTED_NOW, id));

        Object targetMethodResult;

        try {
            targetMethodResult = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            logger.log(Level.WARNING, String.format(Message.THERE_IS_NO_USER_WITH_ID, id));
            return null;
        }

        logger.log(Level.INFO, String.format(Message.USER_WITH_ID_WAS_FOUND_AND_RETURNED, targetMethodResult));
        return targetMethodResult;
    }


    @Around("execution(* com.ioinnes.ru.springboot.testtask.processor.UserProcessor.processDeletingById(..))")
    public Object processDeleteByIdRequestLoggingAspect(ProceedingJoinPoint proceedingJoinPoint) {
        final int id = (int) proceedingJoinPoint.getArgs()[0];

        logger.log(Level.INFO, String.format(Message.DELETE_BY_ID_REQUEST_WILL_BE_EXECUTED_NOW, id));

        String targetMethodResult;

        try {
            targetMethodResult = (String) proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            targetMethodResult = String.format(Message.THERE_IS_NO_USER_WITH_ID, id);

            logger.log(Level.INFO, targetMethodResult);

            return targetMethodResult;
        }

        logger.log(Level.INFO, targetMethodResult);

        return targetMethodResult;
    }


    @Around("execution(* com.ioinnes.ru.springboot.testtask.processor.UserProcessor.processSave(..))")
    private Object processPostRequestAspectLogging(ProceedingJoinPoint proceedingJoinPoint) {
        User user = (User) proceedingJoinPoint.getArgs()[0];

        logger.log(Level.INFO, String.format(Message.POST_USER_WILL_BE_EXECUTED_NOW, user));

        String targetMethodResult;

        try {
            targetMethodResult = (String) proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            if (Message.INVALID_DATA.equals(e.getMessage())) {

                logger.log(Level.WARNING,
                        Message.TRY_TO_ADD_USER_WITH_INVALID_USERNAME_OR_EMAIL);

                return Message.TRY_TO_ADD_USER_WITH_INVALID_USERNAME_OR_EMAIL;
            }
            else if ("Duplicate".equals(e.getMessage())) {

                logger.log(Level.WARNING,
                        Message.THERE_IS_USER_WITH_THE_SAME_USERNAME_OR_EMAIL);

                return Message.THERE_IS_USER_WITH_THE_SAME_USERNAME_OR_EMAIL;
            }
            else if (Message.INVALID_IMAGE.equals(e.getMessage())) {
                logger.log(Level.WARNING,
                        Message.TRY_TO_ADD_USER_WITH_INVALID_IMAGE);

                return Message.TRY_TO_ADD_USER_WITH_INVALID_IMAGE;
            }
            else {
                logger.log(Level.WARNING,
                        Message.SOMETHING_BAD_HAPPENED_WITH_DATABASE);

                return Message.SOMETHING_BAD_HAPPENED_WITH_DATABASE;
            }
        }

        logger.log(Level.INFO, targetMethodResult);

        return targetMethodResult;
    }


    @Around("execution(* com.ioinnes.ru.springboot.testtask.processor.UserProcessor.processGetAll(..))")
    private Object processGetAllWithConditionLoggingAspect(ProceedingJoinPoint proceedingJoinPoint) {

        String status = (String) proceedingJoinPoint.getArgs()[0];
        Timestamp timestamp = (Timestamp) proceedingJoinPoint.getArgs()[1];

        logger.log(Level.INFO, String.format(Message.GET_WITH_CONDITIONS_STATUS_TIMESTAMP_WILL_BE_EXECUTED_NOW, status, timestamp));
        List<User> targetMethodResult;

        try {
            targetMethodResult = (List<User>) proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            if (Message.INVALID_STATUS
                    .equals(e.getMessage())) {
                logger.log(Level.WARNING, Message.TRY_TO_EXECUTE_REQUEST_WITH_WRONG_STATUS_PARAMETER);
                return Collections.emptyList();
            } else {
                logger.log(Level.WARNING, Message.SOMETHING_BAD_HAPPENED_WITH_DATABASE);
                return Collections.emptyList();
            }
        }


        logger.log(Level.INFO, String.format(Message.GET_WITH_CONDITIONS_CONDITIONS_STATUS_S_TIMESTAMP_S_WAS_EXECUTED_SUCCESSFULLY, status, timestamp));

        return targetMethodResult;

    }


    @Around("execution(* com.ioinnes.ru.springboot.testtask.processor.UserProcessor.processChangeStatus(..))")
    private Object processChangeStatusRequestLoggingAspect(ProceedingJoinPoint proceedingJoinPoint) {

        final int id = (int) proceedingJoinPoint.getArgs()[0];
        final String status = (String) proceedingJoinPoint.getArgs()[1];

        logger.log(Level.INFO, String.format(
                Message.PUT_REQUEST_WITH_PARAMETERS_ID_D_STATUS_S_WILL_BE_EXECUTED_NOW, id, status));

        String targetMethodResult;

        try {
            targetMethodResult = (String) proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            if (e instanceof IllegalArgumentException) {

                logger.log(Level.WARNING,
                        Message.TRY_TO_EXECUTE_REQUEST_WITH_WRONG_STATUS_PARAMETER);

                return Message.TRY_TO_EXECUTE_REQUEST_WITH_WRONG_STATUS_PARAMETER;
            }  else if (e instanceof NoSuchElementException) {

                logger.log(Level.WARNING, String.format(
                        Message.THERE_IS_NO_USER_WITH_ID, id));

                return String.format(Message.THERE_IS_NO_USER_WITH_ID, id);
            } else {
                logger.log(Level.WARNING,
                        Message.SOMETHING_WRONG_WITH_ANOTHER_SERVER);
                return Message.SOMETHING_WRONG_WITH_ANOTHER_SERVER;
            }

        }

        logger.log(Level.INFO, targetMethodResult);

        return targetMethodResult;
    }


    @Around("execution(* com.ioinnes.ru.springboot.testtask.processor.UserProcessor.processFindAll(..))")
    private Object processGetAllLoggingAspect(ProceedingJoinPoint proceedingJoinPoint) {
        logger.log(Level.INFO,
                Message.GET_ALL_REQUEST_WILL_BE_EXECUTED_NOW);

        Object targetMethodResult;
        try {
            targetMethodResult = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            logger.log(Level.WARNING, Message.SOMETHING_BAD_HAPPENED_WITH_DATABASE);
            return null;
        }

        logger.log(Level.INFO,
                Message.GET_ALL_REQUEST_WAS_EXECUTED_SUCCESSFULLY);

        return targetMethodResult;
    }

}
