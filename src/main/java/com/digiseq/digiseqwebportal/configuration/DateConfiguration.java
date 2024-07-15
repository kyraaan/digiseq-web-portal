package com.digiseq.digiseqwebportal.configuration;

import com.digiseq.digiseqwebportal.util.DateProvider;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateConfiguration {

  @Bean
  DateProvider dateProvider(Clock clock) {
    return new DateProvider(clock);
  }

  @Bean
  Clock clock() {
    return Clock.systemDefaultZone();
  }
}
