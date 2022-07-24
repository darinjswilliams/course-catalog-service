package com.kotlinspring.controller

import com.kotlinspring.controller.util.instructorDTO
import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.service.InstructorService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [InstructorController::class])
@AutoConfigureWebTestClient
class InstructorControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorService: InstructorService

    @Test
    fun `add instructor to repository`() {
        //setup
        val instructorDTO = InstructorDTO(null, "Craig")

        //mock service call
        every {
            instructorService.createInstructor(any())
        } returns instructorDTO(anyInt(), "")

        //Expected Behavior
        val savedInstructoObj = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody

        assertTrue {
            savedInstructoObj!!.id != null
        }
    }

    @Test
    fun `add instructor validation`() {
     val instructorDTO = InstructorDTO(null, "")

        //mock service call
        every {
            instructorService.createInstructor(any())
        } returns instructorDTO(anyInt(), "")

        //behavior
        val instructorObj = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody
        assertEquals("instructorDTO.name must not be blank", instructorDTO)
//        assertThat(instructorObj, `is`("instructorDTO.name must not be blank"))
    }

}