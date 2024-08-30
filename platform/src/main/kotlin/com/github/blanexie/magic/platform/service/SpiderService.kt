package com.github.blanexie.magic.platform.service

import com.baomidou.mybatisplus.extension.service.IService
import com.github.blanexie.magic.platform.entity.Spider


/**
 *
 * @author xiezc
 * @date 2024/8/26 19:49
 */
interface SpiderService : IService<Spider> {

    fun addTask(spider: Spider)

    fun startTask(taskId: Int)

    //暂停
    fun stopTask(taskId: Int)

    //取消
    fun cancelTask(taskId: Int)



}