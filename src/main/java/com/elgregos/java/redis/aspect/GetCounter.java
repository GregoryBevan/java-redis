package com.elgregos.java.redis.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GetCounter {

	public static int counter;

	@Pointcut("@annotation(com.elgregos.java.redis.aspect.CountGet)")
	public void isAnnotated() {
	}

	@Around("isAnnotated()")
	public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		final Object retVal = joinPoint.proceed();
		counter++;
		return retVal;
	}

}
