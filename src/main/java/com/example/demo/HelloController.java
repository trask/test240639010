package com.example.demo;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.Duration;
import com.microsoft.applicationinsights.telemetry.RemoteDependencyTelemetry;
import com.microsoft.applicationinsights.web.internal.ThreadContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static final String HEXADECIMAL_STRING = "abcdef0123456789";

    private final TelemetryClient client = new TelemetryClient();

    @RequestMapping("/hello")
    public String index() {
        RemoteDependencyTelemetry data = new RemoteDependencyTelemetry();
        String dependencyId = generateDependencyId();
        data.setDuration(new Duration(555));
        data.setType("test type");
        data.setName("test name");
        data.setSuccess(true);
        data.setId(dependencyId);
        String parent = ThreadContext.getRequestTelemetryContext().getHttpRequestTelemetry().getId();
        client.getContext().getOperation().setParentId(parent);
        client.trackDependency(data);
        return "Hello!";
    }

    private String generateDependencyId() {
        final int length = 16;
        StringBuilder sb = new StringBuilder(length);
        for (int i=0; i<length; i++) {
            int index = (int)(length * Math.random());
            sb.append(HEXADECIMAL_STRING.charAt(index));
        }
        return sb.toString();
    }
}
