package com.leskiewicz.schoolsystem.aspect;

import com.leskiewicz.schoolsystem.security.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.security.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.utils.LoggingUtils;
import com.leskiewicz.schoolsystem.utils.StringUtils;
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

  @Pointcut("within(com.leskiewicz.schoolsystem..*) && (@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController))")
  public void controllerMethodsPointcut() {
  }

  @Before("controllerMethodsPointcut()")
  public void logBefore(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    Object[] maskedArgs = new Object[args.length];
    for (int i = 0; i < args.length; i++) {
      maskedArgs[i] = LoggingUtils.maskSensitiveInformation(args[i]);
    }
    logger.info("Entering method: {} with arguments: {}", joinPoint.getSignature().toShortString(),
        maskedArgs);
  }

  @AfterReturning(pointcut = "controllerMethodsPointcut()", returning = "result")
  public void logAfterReturning(JoinPoint joinPoint, Object result) {
    Object maskedResult = LoggingUtils.maskSensitiveInformation(result);
    logger.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(),
        maskedResult);
  }




}
