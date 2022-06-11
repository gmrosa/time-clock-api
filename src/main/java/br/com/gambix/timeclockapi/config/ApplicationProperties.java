package br.com.gambix.timeclockapi.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("singleton")
public class ApplicationProperties {

    @Value("${duration.work}")
    private String workDuration;

    @Value("${duration.lunch}")
    private String lunchDuration;

    @Value("${duration.minLunch}")
    private String minimumLunchDuration;

    @Value("${duration.tolerance}")
    private String toleranceDuration;
}
