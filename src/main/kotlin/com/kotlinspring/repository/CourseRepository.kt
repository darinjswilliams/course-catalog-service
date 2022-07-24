package com.kotlinspring.repository

import com.kotlinspring.entity.Course
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CourseRepository:CrudRepository<Course, Int> {

fun findByNameContaining(courseName: String): List<Course>

//native coding
@Query(value = "SELECT * FROM COURSES where name like %?1%", nativeQuery = true)
fun findCourseByName(courseName: String): List<Course>



}