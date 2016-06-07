/**
 * 2016年5月12日下午2:15:02
 * zhaochuanbin
 */
package com.duantuke.api.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhaochuanbin
 *
 */
//@Component
//@Aspect
public class KafkaAspect {
    
//    private Logger logger = LoggerFactory.getLogger(KafkaAspect.class);
//    
//  //配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
//    @Pointcut("execution(* com.duantuke.api.kafka..*.*(..))")
//    public void aspect(){   }
//    
//    @Around("aspect()")
//    public Object around(ProceedingJoinPoint proceeding) throws Throwable {
//        Object resultObj = null;
//        String methodName = null;
//        String className = null;
//
//       String key = Config.getValue("kafka.consumer.switch");
//       logger.info("kafka.consumer.switch:"+key);
//        if(key.equals("T")){
//            try {
//            resultObj = proceeding.proceed();
//            return resultObj;
//            }catch(Exception e){
//                logger.info("Exception",e);
//                throw e;
//            }
//        }else{
//            return false;
//        }
//    }
    
}
