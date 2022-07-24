package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository,
val instructorService: InstructorService) {

    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {

        val instructorOptional = instructorService.findByInstructorId(courseDTO.instructorId!!)

        if(!instructorOptional.isPresent){
            throw InstructorNotValidException("Instructor Not Valid for the id: ${courseDTO.instructorId}")
        }

        //Transform from CourseDTO to CourseEntity
        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, instructorOptional.get())
        }

        //Save Course Entity to Databasse
        courseRepository.save(courseEntity)

        logger.info("Saved course is $courseEntity")

        //Transform from CourseEntity to CourseDTO and send back to controller
        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category, it.instructor!!.id)
        }
    }

    fun retrieveAllCourse(courseName: String?): List<CourseDTO> {


        //if course name is pass find it, if it find all course
      val courses =  courseName?.let {
            courseRepository.findCourseByName(courseName)
        } ?: courseRepository.findAll()

        return courses
            .map {
                CourseDTO(it.id, it.name, it.category)
            }
    }

    //Return updated course in db
    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
       val existingCourse = courseRepository.findById(courseId)

    return when(existingCourse.isPresent){
          true -> {
              existingCourse.get().
              let {
                  it.name = courseDTO.name
                  it.category = courseDTO.category

                  //Save it database
                  courseRepository.save(it)

                  //a lambda will return the last statement
                  CourseDTO(it.id, it.name, it.category)
              }
          }
          else ->{
              throw CourseNotFoundException("No Course Found fo the passed in Id: $courseId")
          }
      }
    }

    fun deleteCourse(courseId: Int) {

        val existingCourse = courseRepository.findById(courseId)


        when(existingCourse.isPresent){
            true -> {
                existingCourse.get().
                let {

                    courseRepository.deleteById(courseId)

                    //a lambda will return the last statement
                    CourseDTO(it.id, it.name, it.category)
                }
            }
            else ->{
                throw CourseNotFoundException("No Course Found fo the passed in Id: $courseId")
            }
        }
    }
}
