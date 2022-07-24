package com.kotlinspring.controller.util

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.entity.Instructor

fun courseEntityList() = listOf(
    Course(
        null,
        "Build Restful API's using SpringBoot and Kotlin",
        "Development"
    ),
    Course(
        null,
        "Build Reactive Microservices using Spring WebFlux/SpringBoot",
        "Development"
    ),
    Course(
        null,
        "Wiremock for Java Developers",
        "Development"
    )
)

fun instructorEntityList() = listOf(
    Instructor(
        null,
        "Maria"
    ),
    Instructor(
        null,
        "Darin"
    ),
    Instructor(
        null,
        "Gunner"
    )
)

fun courseDTO(
    id: Int? = null,
    name: String = "Build Restful Api's using Spring Boot and kotlin",
    category: String = "Dilip Sundarraj"
//    instructorId: Int? = 1
) = CourseDTO(
    id,
    name,
    category
//    instructorId
)

fun instructorDTO(
    id: Int? = null,
    name: String
) = InstructorDTO(
    id,
    name
)

fun courseEntityList(instructor: Instructor? = null) = listOf(

    Course(
        null,
        "Build Restful API's using SpringBoot and Kotlin",
        "Development",
        instructor
    ),
    Course(
        null,
        "Build Reactive Microservices using Spring WebFlux/SpringBoot",
        "Development",
        instructor
    ),
    Course(
        null,
        "Wiremock for Java Developers",
        "Development",
        instructor
    )
)

fun instructorEntity(name: String = "Maria Williams") = Instructor(null, name)