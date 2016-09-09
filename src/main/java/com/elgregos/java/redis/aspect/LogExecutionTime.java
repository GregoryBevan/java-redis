package com.elgregos.java.redis.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

@Aspect
public class LogExecutionTime {

	private static final String LOG_MESSAGE_FORMAT = "execution time";
	private static final Logger LOG = LoggerFactory.getLogger(LogExecutionTime.class);

	@Around("myPointCut()")
	public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		final Object retVal = joinPoint.proceed();

		stopWatch.stop();

		logExecutionTime(joinPoint, stopWatch);
		return retVal;
	}

	@Pointcut("execution(something goes here")
	public void myPointCut() {
	}

	private void logExecutionTime(ProceedingJoinPoint joinPoint, StopWatch stopWatch) {
		final String logMessage = String.format(LOG_MESSAGE_FORMAT, joinPoint.getTarget().getClass().getName(),
				joinPoint.getSignature().getName(), stopWatch.getTotalTimeSeconds());
		LOG.info(logMessage.toString());
	}
}
