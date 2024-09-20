package com.github.blanexie.magic.platform.mapper

import com.github.blanexie.magic.platform.entity.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository


/**
 *
 * @author xiezc
 * @date 2024/8/26 20:15
 */
@Repository
interface TaskRepository : JpaRepository<Task, Int>, JpaSpecificationExecutor<Task> {

    fun findByStatus(status: Int): List<Task>

}