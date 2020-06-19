package com.scrumpoker.aop;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceLoggerAspectTest {

	@Mock
	private JoinPoint joinPoint;
	
	private ServiceLoggerAspect serviceLoggerAspect;

	@BeforeEach
	public void setUp() {
		this.serviceLoggerAspect = new ServiceLoggerAspect();
	}

	@Test
	public void beforeServicesShouldBeExecuted() throws Throwable {
		serviceLoggerAspect.beforeServices(joinPoint);
		
		verify(joinPoint, times(1)).getSignature();
		verify(joinPoint, times(1)).getArgs();
	}
	
	@Test
	public void afterServicesShouldBeExecuted() throws Throwable {
		serviceLoggerAspect.afterServices(joinPoint);
		
		verify(joinPoint, times(1)).getSignature();
		verify(joinPoint, times(1)).getArgs();
	}

}
