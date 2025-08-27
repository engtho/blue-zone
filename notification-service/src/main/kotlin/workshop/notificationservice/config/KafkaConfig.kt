package workshop.notificationservice.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

/**
 * Kafka configuration for Notification Service.
 *
 * This configuration class sets up Kafka topics and other Kafka-related beans. In production, you
 * might also configure:
 * - Custom serializers/deserializers
 * - Consumer and producer properties
 * - Error handling and retry policies
 * - Security settings (SSL, SASL)
 */
@Configuration
class KafkaConfig {

    /** Create the notifications topic. This topic is used to publish notification events. */
    @Bean
    fun notificationsTopic(): NewTopic =
            TopicBuilder.name("notifications").partitions(1).replicas(1).build()
}
