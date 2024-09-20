package com.github.blanexie.magic.platform.service.impl

import com.github.blanexie.magic.platform.common.exception.BusinessException
import com.github.blanexie.magic.platform.common.exception.ErrorCode
import com.github.blanexie.magic.platform.common.magic.TaskManager
import com.github.blanexie.magic.platform.entity.Task
import com.github.blanexie.magic.platform.mapper.TaskRepository
import com.github.blanexie.magic.platform.service.TaskService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 *
 * @author xiezc
 * @date 2024/8/29 17:15
 */
@Service
class TaskServiceImpl(
        val taskManager: TaskManager,
        val taskRepository: TaskRepository
) : TaskService {

    override fun addTask(task: Task) {
        taskRepository.save(task)
    }

    override fun startTask(taskId: Int) {
        val spider = taskRepository.findByIdOrNull(taskId) ?: throw BusinessException(ErrorCode.PARAM_ERROR)
        val task = taskManager.getOrCreate(spider)
        if (task.status == us.codecraft.webmagic.Spider.Status.Init) {
            task.start()
            spider.status = 1
            taskRepository.save(spider)
        }
    }

    /**
     * 暂停
     */
    override fun stopTask(taskId: Int) {
        val spider = taskRepository.findById(taskId).get()
        val task = taskManager.getOrCreate(spider)
        if (task.status == us.codecraft.webmagic.Spider.Status.Running) {
            task.stop()
            spider.status = 3
            taskRepository.save(spider)
        }
    }

    override fun cancelTask(taskId: Int) {
        val spider = taskRepository.findById(taskId).get()
        val task = taskManager.getOrCreate(spider)
        if (task.status == us.codecraft.webmagic.Spider.Status.Running) {
            task.stop()
            spider.status = 2
            taskRepository.save(spider)
        }
    }

    override fun findSpiders(status: Int): List<Task> {
        return taskRepository.findByStatus(status)
    }

}