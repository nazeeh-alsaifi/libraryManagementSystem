package cc.maids.libms;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class ExecutionTimeAspect {

    @Pointcut("execution(* cc.maids.libms.*.*Controller.*(..))")
    public void timeControllersMethods() {
    }

    @Pointcut("execution(* cc.maids.libms.*.*Service.*(..))")
    public void timeServiceMethods() {
    }

    @Pointcut("execution(* cc.maids.libms.*.*Repository.*(..))")
    public void timeRepositoryMethods() {
    }

    @Around("timeControllersMethods() || timeServiceMethods() || timeRepositoryMethods()")
    public Object calculateExecutionTimeAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long t1 = System.currentTimeMillis();
        Object object = joinPoint.proceed();
        long t2 = System.currentTimeMillis();
        log.info(joinPoint.getSignature().toShortString() + " execution time " + (t2 - t1) + "ms");
        return object;
    }
}
