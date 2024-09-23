package com.github.blanexie.magic.platform.controller

import com.github.blanexie.magic.platform.common.util.WebResult
import com.github.blanexie.magic.platform.service.TaskService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 *
 * @author xiezc
 * @date 2024/8/30 19:03
 */
@RestController
@RequestMapping("task")
class TaskController(
        val taskService: TaskService
) {

    @GetMapping("findAll")
    fun findAll(): WebResult {
        val spiders = taskService.findTask(0)
        return WebResult.success(spiders)
    }

}