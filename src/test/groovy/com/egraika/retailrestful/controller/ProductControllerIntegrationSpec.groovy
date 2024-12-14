/*package com.egraika.retailrestful.controller

import com.egraika.retailrestful.MyRetailRestfulApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification
import spock.lang.Stepwise

@Testcontainers
@Stepwise
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MyRetailRestfulApplication
)
class ProductControllerIntegrationSpec extends Specification {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6")

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl)
        registry.add("external.api.url", { -> "http://localhost:8080/api/product/" })
    }

    def setupSpec() {
        mongoDBContainer.start()
    }

    def cleanupSpec() {
        mongoDBContainer.stop()
    }

    def "test example integration test"() {
        expect:
        true
    }
}*/