package com.kotlinspring.controller

import com.kotlinspring.controller.util.courseDTO
import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMockk: CourseService
    

    @Test
    fun `create a valid course `() {
        val courseDTO = CourseDTO(null, "Build Restful API's using SpringBoot and Kotlin",
            "Darin Williams", anyInt()
        )

        //Mock the service call using every,
        every {
            courseServiceMockk.addCourse(any())
        } returns courseDTO(id = 4)

        val savedCouseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedCouseDTO!!.id != null
        }
    }

    @Test
    fun `retrieve all courses`() {

        //When testing list use returnsMany
        every { courseServiceMockk.retrieveAllCourse(any()) }.returnsMany(
            listOf(
                courseDTO(id = 2),
                courseDTO(id = 4, "Build Restful API's using SpringBoot and Java")
            )
        )


        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            //when dealing with a list use expectBodyList
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courseDTOs $courseDTOs")
        assertEquals(2, courseDTOs!!.size)
    }

    @Test
    fun `update course with instructor`() {
        val course = Course(
            null,
            "Build Restful API's using SpringBoot and Kotlin",
            "Development"
        )

        every { courseServiceMockk.updateCourse(any(), any()) } returns
                courseDTO(id = 3, name = "Build Restful API's using SpringBoot and Kotlin1")

        val updatedCourseDTO = CourseDTO(
            null,
            "Build Restful API's using SpringBoot and Kotlin1",
            "Development"
        )

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 3)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals("Build Restful API's using SpringBoot and Kotlin1", updatedCourse!!.name)
    }

    @Test
    fun `delete course`() {


        //mock simulating a call that does not return a value use just runs
        every { courseServiceMockk.deleteCourse(any()) } just runs
        val updatedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent

    }

    @Test
    fun `add course validation on category must not be blank`() {
        val courseDTO = CourseDTO(null, "", "", anyInt())

        //Mock the service call using every,
        every {
            courseServiceMockk.addCourse(courseDTO)
        } returns courseDTO(anyInt())

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("courseDTO.category must not be blank, courseDTO.name must not be blank", response)
    }

    @Test
    fun `add course runtime exception`() {


        val courseDTO = CourseDTO(null, "Build Restful API's using SpringBoot and Kotlin1",
            "Darin W", anyInt()
        )

        //Mock the service call using every,
        val errorMessage = "Unexpected Error occurred"

        every {
            courseServiceMockk.addCourse(any())
        } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMessage, response)
    }
}