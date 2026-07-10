package cn.iocoder.yudao.module.payment.common.util;

import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

/**
 * TraceId 工具类
 * <p>
 * 用于在异步场景（MQ消费、线程池、XXL-Job）中传递 TraceId。
 * 同步链路（Feign/Gateway）由 Spring Cloud Sleuth 自动处理，无需手动传递。
 */
public class TraceIdUtils {

    private static final String TRACE_ID_KEY = "traceId";
    private static final String BIZ_TRACE_ID_KEY = "bizTraceId";

    /**
     * 获取当前线程的 traceId
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * 设置 traceId 到 MDC
     */
    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 获取业务 traceId（如 trade_id、request_id）
     */
    public static String getBizTraceId() {
        return MDC.get(BIZ_TRACE_ID_KEY);
    }

    /**
     * 设置业务 traceId
     */
    public static void setBizTraceId(String bizTraceId) {
        MDC.put(BIZ_TRACE_ID_KEY, bizTraceId);
    }

    /**
     * 生成新的 traceId
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 确保当前线程有 traceId（如果没有则生成一个）
     */
    public static String ensureTraceId() {
        String traceId = getTraceId();
        if (traceId == null || traceId.isEmpty()) {
            traceId = generateTraceId();
            setTraceId(traceId);
        }
        return traceId;
    }

    /**
     * 捕获当前 MDC 上下文（用于传递给异步线程）
     */
    public static Map<String, String> captureMdcContext() {
        return MDC.getCopyOfContextMap();
    }

    /**
     * 恢复 MDC 上下文（在异步线程中调用）
     */
    public static void restoreMdcContext(Map<String, String> contextMap) {
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
    }

    /**
     * 清除 MDC 上下文
     */
    public static void clear() {
        MDC.clear();
    }
}
