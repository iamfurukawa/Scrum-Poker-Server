package com.scrumpoker.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import lombok.extern.log4j.Log4j2;

@Aspect
@Configuration
@Log4j2
public class ServiceLoggerAspect {

	@Before("execution(* com.scrumpoker.service..*.*(..))")
	public void beforeServices(final JoinPoint joinPoint) {
		log.info("+ {}, {}", joinPoint.getSignature(), joinPoint.getArgs());
	}
	
	@After("execution(* com.scrumpoker.service..*.*(..))")
	public void afterServices(final JoinPoint joinPoint) {
		log.info("- {}, {}", joinPoint.getSignature(), joinPoint.getArgs());
	}
}