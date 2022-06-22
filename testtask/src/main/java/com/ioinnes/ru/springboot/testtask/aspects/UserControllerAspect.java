package com.ioinnes.ru.springboot.testtask.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;


@Aspect
@Component
public class UserControllerAspect {
    private final Logger logger = Logger.getLogger(UserControllerAspect.class.getSimpleName());
    @Before("execution(* com.ioinnes.ru.springboot.testtask.controller.UserController.find*(..))")
    private void logBeforeGetRequestExecuting(JoinPoint joinPoint) {
        logger.log(Level.INFO, String.format("Get request named %s will be executed now",
                joinPoint.getSignature().getName()));
    }

    @AfterReturning("execution(* com.ioinnes.ru.springboot.testtask.controller.UserController.find*(..))")
    private void logAfterGetRequestExecuting(JoinPoint joinPoint) {
        logger.log(Level.INFO, String.format("Get request named %s was executed",
                joinPoint.getSignature().getName()));
    }

    @Before("execution(* com.ioinnes.ru.springboot.testtask.controller.UserController.save*(..))")
    private void logBeforePostRequestExecuting(JoinPoint joinPoint) {
        logger.log(Level.INFO, String.format("Post request named %s will be executed now",
                joinPoint.getSignature().getName()));
    }

    @AfterReturning("execution(* com.ioinnes.ru.springboot.testtask.controller.UserController.save*(..))")
    private void logAfterPostRequestExecuting(JoinPoint joinPoint) {
        logger.log(Level.INFO, String.format("Post request named %s was executed",
                joinPoint.getSignature().getName()));
    }

    @Before("execution(* com.ioinnes.ru.springboot.testtask.controller.UserController.delete*(..))")
    private void logBeforeDeleteRequestExecuting(JoinPoint joinPoint) {
        logger.log(Level.INFO, String.format("Delete request named %s will be executed now",
                joinPoint.getSignature().getName()));
    }

    @AfterReturning("execution(* com.ioinnes.ru.springboot.testtask.controller.UserController.delete*(..))")
    private void logAfterDeleteRequestExecuting(JoinPoint joinPoint) {
        logger.log(Level.INFO, String.format("Delete request named %s was executed",
                joinPoint.getSignature().getName()));
    }



    @Before("execution(* com.ioinnes.ru.springboot.testtask.controller.UserController.change*(..))")
    private void logBeforePutRequestExecuting(JoinPoint joinPoint) {
        logger.log(Level.INFO, String.format("Put request named %s will be executed now",
                joinPoint.getSignature().getName()));
    }

    @AfterReturning("execution(* com.ioinnes.ru.springboot.testtask.controller.UserController.change*(..))")
    private void logAfterPutRequestExecuting(JoinPoint joinPoint) {
        logger.log(Level.INFO, String.format("Put request named %s was executed",
                joinPoint.getSignature().getName()));
    }


}
