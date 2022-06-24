package com.ioinnes.ru.springboot.testtask.aspects;


//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;


//// #TODO don't work properly
//@Aspect
//public class ImageControllerAspect {
//
//    private final Logger logger = Logger.getLogger(ImageControllerAspect.class.getSimpleName());
//
//    @Before("execution(* com.ioinnes.ru.springboot.testtask.processor.ImageProcessor.upload*(..))")
//    private void logBeforeSaveImageRequestExecuting(JoinPoint joinPoint) {
//        logger.log(Level.INFO, String.format("Post request named %s will be executed now",
//                joinPoint.getSignature().getName()));
//    }
//
//    @AfterReturning("execution(* com.ioinnes.ru.springboot.testtask.processor.ImageProcessor.upload*(..))")
//    private void logAfterSaveImageRequestExecuting(JoinPoint joinPoint) {
//        logger.log(Level.INFO, String.format("Post request named %s was executed",
//                joinPoint.getSignature().getName()));
//    }
//
//
//
//}
