package com.kotlinspring.controller

import com.kotlinspring.controller.util.instructorEntityList
import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.repository.InstructorRepository
import mu.KLogging
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class InstructorControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    companion object: KLogging()

    @BeforeEach
    fun setUp() {
        instructorRepository.deleteAll()
        val instructors = instructorEntityList()
        instructorRepository.saveAll(instructors)
    }

    @Test
    fun `add a new instructor`() {
         val instructorDTO = InstructorDTO(null, "Jimmy")

      val instructor =  webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody
        logger.info("Instructor : ${instructor?.id}")

        assertThat(instructor!!.id, `is`(notNullValue()))
    }

    @Test
   fun `add instructor validation`() {
        val instructorDTO = InstructorDTO(null, "")

        val instructor = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

//        assertEquals("instructorDTO.name must not be null", instructor)
        assertThat("instructorDTO.name must not be null" ,`is`(instructor))
    }
}