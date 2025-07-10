package kr.or.ddit.util.p6spy;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class CustomAbbreviatorConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String fullClassName = event.getLoggerName();
        if (fullClassName == null) return "";

        String[] parts = fullClassName.split("\\.");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parts.length - 1; i++) {
            if (i < 4) {
                // kr.or.ddit.xxxx 앞의 4개 패키지는 축약
                sb.append(parts[i].charAt(0)).append(".");
            } else {
                // 나머지는 그대로
                sb.append(parts[i]).append(".");
            }
        }

        // 클래스 이름은 풀로 출력
        sb.append(parts[parts.length - 1]);

        return sb.toString();
}
}