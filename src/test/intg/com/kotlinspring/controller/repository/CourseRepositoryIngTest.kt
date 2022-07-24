package com.kotlinspring.controller.repository

import com.kotlinspring.controller.util.courseEntityList
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIngTest {


    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun findByNameContaining() {
        val courses = courseRepository.findByNameContaining("SpringBoot")
        logger.info("courses: $courses")

        assertEquals(2, courses.size)
    }

    @Test
    fun findCourseByName() {
        val courses = courseRepository.findCourseByName("SpringBoot")
        logger.info("courses: $courses")

        assertEquals(2, courses.size)
    }

    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun `find course by name using parameter test`(name: String, expectedSize: Int) {
        val courses = courseRepository.findCourseByName(name)
        logger.info("courses: $courses")

        assertEquals(expectedSize, courses.size)
    }

    companion object: KLogging() {

        @JvmStatic
        fun courseAndSize(): Stream<Arguments>{
            //provide input = springboot, and output = 2
            return Stream.of(Arguments.arguments("SpringBoot", 2),
                Arguments.arguments("Wiremock", 1)
            )
        }
    }

}


