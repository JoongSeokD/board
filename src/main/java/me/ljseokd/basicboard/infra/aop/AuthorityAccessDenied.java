package me.ljseokd.basicboard.infra.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.event.Event;
import me.ljseokd.basicboard.modules.event.EventRepository;
import me.ljseokd.basicboard.modules.notice.Notice;
import me.ljseokd.basicboard.modules.notice.NoticeRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorityAccessDenied {


    private final EventRepository eventRepository;
    private final NoticeRepository noticeRepository;

    @Before("execution(* me.ljseokd.basicboard.modules.*.*Controller.*UpdateForm*(..)) || execution(* me.ljseokd.basicboard.modules.*.*Controller.*Delete(..))")
    public void authorityAccessDenied(JoinPoint joinPoint) throws Throwable {
        Account account = null;
        Long id = null;
        HttpServletRequest request = null;
        Object[] parameters = joinPoint.getArgs();

        for (Object parameter : parameters) {
            if (parameter instanceof Account){
                account = (Account) parameter;
            } else if (parameter instanceof Long){
                id = (Long) parameter;
            } else if (parameter instanceof HttpServletRequest){
                request = (HttpServletRequest) parameter;
            }
        }

        String remoteAddr = getClientIpAddr(request);
        String writer = getWriter(id, Class.forName(getClassPath(joinPoint)));
        if (!writer.equals(account.getNickname())){
            log.error("[userId={} : userIp={}] : 계정의 비 정상적인 접근입니다. {}", account.getNickname(), remoteAddr, joinPoint.getStaticPart().toLongString());
            throw new IllegalArgumentException(String.format("[%s] : 계정의 비 정상적인 접근입니다. %s", account.getNickname(), joinPoint.getStaticPart().toLongString()));
        }
    }

    private String getClassPath(JoinPoint joinPoint) {
        String packageName = joinPoint.getTarget().getClass().getPackageName();
        int basePackageIdx = packageName.lastIndexOf(".") + 1;
        String basePackageName = packageName.substring(basePackageIdx);
        char c = basePackageName.charAt(0);
        String upperCase = String.valueOf(c).toUpperCase();
        String getRepository = packageName + "." + upperCase + basePackageName.substring(1) + "Repository";
        return getRepository;
    }

    private String getWriter(Long id, Class<?> repository) throws Throwable {
        Long finalId = id;
        Optional findById;
        if (isEqualsDomainRepository(repository, EventRepository.class)){
            findById = (Optional) getInvoke(id,repository, eventRepository, "findByAccountWithId");
            Event event = (Event) getEntity(finalId, findById);
            return event.getCreatedBy().getNickname();
        } else if (isEqualsDomainRepository(repository, NoticeRepository.class)){
            findById = (Optional) getInvoke(id, repository, noticeRepository, "findNoticeView");
            Notice notice = (Notice) getEntity(finalId, findById);
            return notice.getAccount().getNickname();
        }
        return null;
    }
    private boolean isEqualsDomainRepository(Class<?> repository, Class injectRepository) {
        return repository == injectRepository;
    }

    private Object getInvoke(Long id, Class<?> repository, CrudRepository injectRepository, String findMethod) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return repository.getMethod(findMethod, Long.class).invoke(injectRepository, id);
    }

    private Object getEntity(Long finalId, Optional findById) throws Throwable {
        return findById
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(finalId)));
    }


    private String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
