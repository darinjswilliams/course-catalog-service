package com.kotlinspring.entity

import javax.persistence.*

@Entity
@Table(name = "INSTRUCTORS")
data class Instructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    var name: String,
    @OneToMany(
        mappedBy = "instructor",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    // by default hold empty list, but once the course get add,
    // it will get populated
    var course: List<Course> = mutableListOf()
)