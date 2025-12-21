package com.sprint.mission.discodeit.config;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@Order(0)
public class LogAop {

    // 컨트롤러, 서비스 어노테이션 붙인것들만
    @Pointcut("within(@org.springframework.stereotype.Service *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void appLayers() {
    }

    //CRUD + find
    @Pointcut("execution(* *..*create*(..)) || execution(* *..*update*(..)) || " +
            "execution(* *..*delete*(..)) || execution(* *..*find*(..)) || " +
            "execution(* *..*download*(..))")
    public void crudMethods() {
    }

    // 실제 실행  위조건 포함 실행
    @Around("appLayers() && crudMethods()")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        String sig = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();

        log.info("→ 실행 시작: {} ({} ms)", sig, start);

        //실행중
        try {
            Object result = pjp.proceed();
            long took = System.currentTimeMillis();
            log.info("실행 완료: {} ({} ms)", sig, took);
            return result;
        } catch (IllegalArgumentException e) {
            Object[] args = pjp.getArgs();
            log.error("잘못된 값: {} args={} errmsg={}", sig, Arrays.toString(args), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("실행 실패: {} err={}", sig, e.toString());
            throw e;
        }
    }
}
