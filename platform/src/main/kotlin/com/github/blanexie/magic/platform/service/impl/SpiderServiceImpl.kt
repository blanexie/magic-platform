package com.github.blanexie.magic.platform.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.github.blanexie.magic.platform.common.TaskManager
import com.github.blanexie.magic.platform.entity.Spider
import com.github.blanexie.magic.platform.mapper.SpiderMapper
import com.github.blanexie.magic.platform.service.SpiderService
import org.springframework.stereotype.Service


/**
 *
 * @author xiezc
 * @date 2024/8/29 17:15
 */
@Service
class SpiderServiceImpl(
        val taskManager: TaskManager,
) : SpiderService, ServiceImpl<SpiderMapper, Spider>() {


    override fun addTask(spider: Spider) {
        this.save(spider)
    }

    override fun startTask(taskId: Int) {
        val spider = this.getById(taskId)
        val task = taskManager.getOrCreate(spider)
        if (task.status == us.codecraft.webmagic.Spider.Status.Init) {
            task.start()
            spider.status = 1
            this.updateById(spider)
        }
    }

    /**
     * 暂停
     */
    override fun stopTask(taskId: Int) {
        val spider = this.getById(taskId)
        val task = taskManager.getOrCreate(spider)
        if (task.status == us.codecraft.webmagic.Spider.Status.Running) {
            task.stop()
            spider.status = 3
            this.updateById(spider)
        }
    }

    override fun cancelTask(taskId: Int) {
        val spider = this.getById(taskId)
        val task = taskManager.getOrCreate(spider)
        if (task.status == us.codecraft.webmagic.Spider.Status.Running) {
            task.stop()
            spider.status = 2
            this.updateById(spider)
        }
    }

}