package com.leskiewicz.schoolsystem.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Pointcut(
      "within(com.leskiewicz.schoolsystem..*) && (@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController))")
  public void controllerMethodsPointcut() {}

  @Pointcut(
      "within(com.leskiewicz.schoolsystem..*) && @within(org.springframework.stereotype.Service)")
  public void nonControllerMethodsPointcut() {}

  // region Controller logs
  @Before("controllerMethodsPointcut()")
  public void logInfoBefore(JoinPoint joinPoint) {
    // Mask each argument
    Object[] maskedArgs = maskArguments(joinPoint.getArgs());
    logger.info(
        "Entering method: {} with arguments: {}",
        joinPoint.getSignature().toShortString(),
        maskedArgs);
  }

  @AfterReturning(pointcut = "controllerMethodsPointcut()", returning = "result")
  public void logInfoAfterReturning(JoinPoint joinPoint, Object result) {
    // Mask result
    Object maskedResult = LoggingUtils.maskSensitiveInformation(result);
    logger.info(
        "Exiting method: {} with result: {}",
        joinPoint.getSignature().toShortString(),
        maskedResult);
  }
  // endregion

  // region Service logs
  @Before("nonControllerMethodsPointcut()")
  public void logDebugBefore(JoinPoint joinPoint) {
    // Mask each argument
    Object[] maskedArgs = maskArguments(joinPoint.getArgs());
    logger.debug(
        "Entering method: {} with arguments: {}",
        joinPoint.getSignature().toShortString(),
        maskedArgs);
  }

  @AfterReturning(pointcut = "nonControllerMethodsPointcut()", returning = "result")
  public void logDebugAfterReturning(JoinPoint joinPoint, Object result) {
    // Mask result
    Object maskedResult = LoggingUtils.maskSensitiveInformation(result);
    logger.debug(
        "Exiting method: {} with result: {}",
        joinPoint.getSignature().toShortString(),
        maskedResult);
  }
  // endregion

  private Object[] maskArguments(Object[] args) {
    Object[] maskedArgs = new Object[args.length];
    for (int i = 0; i < args.length; i++) {
      // Mask each argument
      maskedArgs[i] = LoggingUtils.maskSensitiveInformation(args[i]);
    }
    return maskedArgs;
  }
}
