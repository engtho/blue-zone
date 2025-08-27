package workshop.ticketservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

/**
 * Configuration class for ticket service beans.
 *
 * This class centralizes configuration and bean creation. Useful for:
 * - External service configurations
 * - Custom serializers/deserializers
 * - Feature flags
 * - Environment-specific settings
 */
@Configuration
class TicketServiceConfig {

    /**
     * RestClient bean for external service calls. Centralized configuration allows for:
     * - Custom timeouts
     * - Interceptors
     * - Error handling
     * - Metrics
     */
    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
                // Add custom configurations here:
                // .requestInterceptor(...)
                // .defaultStatusHandler(...)
                .build()
    }
}
