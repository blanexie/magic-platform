package com.github.blanexie.magic.service.platform.common.magic

import com.alibaba.fastjson.JSON
import com.github.blanexie.magic.platform.entity.Result
import com.github.blanexie.magic.platform.service.ResultService
import com.github.blanexie.magic.service.platform.service.SchedulerService
import org.slf4j.LoggerFactory
import us.codecraft.webmagic.ResultItems
import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.Pipeline
import java.time.LocalDateTime

/**
 *
 * @author xiezc
 * @date 2024/8/30 10:00
 */

class MagicPagePipeline(
        val resultService: ResultService,
        val schedulerService: com.github.blanexie.magic.service.platform.service.SchedulerService
) : Pipeline {

    val log = LoggerFactory.getLogger(MagicPagePipeline::class.java)

    override fun process(resultItems: ResultItems, task: Task) {
        val extractResult = resultItems.get<Boolean>("extractResult")
        val schedulerId = resultItems.get<Int>("schedulerId")
        val requestUrl = resultItems.get<String>("requestUrl")
        val fetchCount = resultItems.get<Int>("fetchCount")
        if (extractResult) {
            val result = Result(null, schedulerId, JSON.toJSONString(resultItems), LocalDateTime.now(), LocalDateTime.now())
            resultService.save(result)
        } else {
            //不予保存结果的页面
            log.info("不予保存结果的页面 schedulerId:{} url:{} ", schedulerId, requestUrl)
        }
        //修改scheduler的状态
        schedulerService.updateFetchCount(fetchCount, schedulerId)
    }


}