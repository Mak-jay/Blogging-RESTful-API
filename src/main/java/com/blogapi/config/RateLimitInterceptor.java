package com.blogapi.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RateLimitInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);
    
    private final int maxRequestsPerMinute;
    private final Map<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

    public RateLimitInterceptor(int maxRequestsPerMinute) {
        this.maxRequestsPerMinute = maxRequestsPerMinute;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIpAddress(request);
        String key = clientIp + ":" + request.getRequestURI();
        
        RequestCounter counter = requestCounters.computeIfAbsent(key, k -> new RequestCounter());
        
        if (counter.isLimitExceeded()) {
            logger.warn("Rate limit exceeded for IP: {} on endpoint: {}", clientIp, request.getRequestURI());
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded. Please try again later.");
            return false;
        }
        
        counter.increment();
        return true;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private long windowStart = System.currentTimeMillis();
        private static final long WINDOW_SIZE = 60000; // 1 minute

        public boolean isLimitExceeded() {
            long now = System.currentTimeMillis();
            if (now - windowStart > WINDOW_SIZE) {
                count.set(0);
                windowStart = now;
            }
            return count.get() >= 100; // Default limit
        }

        public void increment() {
            count.incrementAndGet();
        }
    }
} 