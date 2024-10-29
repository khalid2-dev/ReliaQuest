package com.reliaquest.api.utility;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

	static Random random = new Random(); 
    private static final int MAX_REQUESTS = random.nextInt(15-5)+5;
    private static final long TIME_WINDOW = 30 * 1000;

    private final Map<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        long currentTime = Instant.now().toEpochMilli();

        requestCounts.putIfAbsent(clientIp, new RequestInfo(0, currentTime));

        RequestInfo info = requestCounts.get(clientIp);

        if (currentTime - info.timestamp >= TIME_WINDOW) {
            info.timestamp = currentTime;
            info.count = 0;
        }

        if (info.count >= MAX_REQUESTS) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded. Try again later.");
            return false;
        }

        info.count++;
        return true;
    }

    private static class RequestInfo {
        int count;
        long timestamp;

        RequestInfo(int count, long timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }
}
