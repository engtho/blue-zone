package workshop.alarmservice.config

import org.springframework.context.annotation.Configuration

/**
 * Configuration class for alarm service.
 *
 * This class centralizes configuration for the alarm service. Can be extended with:
 * - Kafka configuration
 * - Scheduling configuration
 * - Custom serializers
 * - Monitoring and metrics
 */
@Configuration
class AlarmServiceConfig {

    // Add custom beans and configuration here
    // For example:
    // @Bean
    // fun alarmMetrics(): MeterRegistry { ... }

}
