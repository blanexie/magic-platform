package com.github.blanexie.magic.platform.service

import com.github.blanexie.magic.platform.entity.Task


/**
 *
 * @author xiezc
 * @date 2024/8/26 19:49
 */
interface TaskService {

    fun addTask(task: Task)

    fun startTask(taskId: Int)

    //暂停
    fun stopTask(taskId: Int)

    //取消
    fun cancelTask(taskId: Int)

    fun findTask(status: Int): List<Task>

}