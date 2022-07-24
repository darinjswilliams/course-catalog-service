package com.kotlinspring.controller.repository

import com.kotlinspring.controller.util.instructorEntityList
import com.kotlinspring.entity.Instructor
import com.kotlinspring.repository.InstructorRepository
import mu.KLogging
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.provider.Arguments
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
class InstructorRepositoryTest {

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
     fun setUp() {
        instructorRepository.deleteAll()
        val instructors = instructorEntityList()
        instructorRepository.saveAll(instructors)
    }

    @Test
   fun `save instructor to database`() {
        val instructorEntity = Instructor(null, "Jessie" )

        val fnd = instructorRepository.save(instructorEntity)
        logger.info("save instructor: $fnd")

        assertThat(fnd.id, `is`(4))
    }

    companion object: KLogging(){

        fun instructorSize(): Stream<Arguments> {
           return Stream.of(Arguments.arguments("Darin", 1),
            Arguments.arguments("Maria", 1),
            Arguments.arguments("Gunner", 1))
        }
    }
}