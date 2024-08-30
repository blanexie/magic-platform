package com.github.blanexie.magic.platform.common

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
class TaskManager(
        val schedulerService: SchedulerService,
        val resultService: ResultService
) {


    val spiders = mutableMapOf<Int, Spider>()


    fun getOrCreate(spider: com.github.blanexie.magic.platform.entity.Spider): Spider {
        return spiders.computeIfAbsent(spider.id) {
            val task = Spider.create(MagicPageProcessor(spider))
                    .setScheduler(DbQueueScheduler(spider, schedulerService))
                    .addPipeline(MagicPagePipeline(resultService, schedulerService))
                    .thread(1)
            spider.startUrls.forEach {
                task.addUrl(it)
            }
            task
        }
    }

}