package com.github.blanexie.magic.platform.common.magic

import com.github.blanexie.magic.platform.entity.Task
import com.github.blanexie.magic.platform.service.ResultService
import com.github.blanexie.magic.platform.service.SchedulerService
import org.springframework.stereotype.Component
import us.codecraft.webmagic.Spider


/**
 *
 * @author xiezc
 * @date 2024/8/30 14:07
 */
@Component
class TaskManager(val schedulerService: SchedulerService, val resultService: ResultService) {

    val spiders = mutableMapOf<Int, Spider>()

    fun getOrCreate(task: Task): Spider {
        return spiders.computeIfAbsent(task.id) {
            val spider = Spider.create(MagicPageProcessor(task))
                    .setScheduler(DbQueueScheduler(task, schedulerService))
                    .addPipeline(MagicPagePipeline(resultService, schedulerService))
                    .thread(1)
            task.startUrls.forEach {
                spider.addUrl(it)
            }
            spider
        }
    }

}