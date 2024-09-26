package com.github.blanexie.magic.service.platform.common.magic

import cn.hutool.core.convert.Convert
import com.github.blanexie.magic.platform.entity.Scheduler

import com.github.blanexie.magic.service.platform.service.SchedulerService
import us.codecraft.webmagic.Request
import us.codecraft.webmagic.Task
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler
import us.codecraft.webmagic.scheduler.MonitorableScheduler
import java.io.Closeable
import java.time.LocalDateTime
import java.util.concurrent.LinkedBlockingQueue


/**
 *
 * @author xiezc
 * @date 2024/8/26 19:55
 */
class DbQueueScheduler(
        val task: com.github.blanexie.magic.platform.entity.Task,
        val schedulerService: com.github.blanexie.magic.service.platform.service.SchedulerService
) : DuplicateRemovedScheduler(), MonitorableScheduler, Closeable {

    private val queue = LinkedBlockingQueue<Request>()

    private var schedulerId: Int = 0

    override fun poll(task: Task?): Request {
        //检查队列中数量是否已经空了
        if (queue.isEmpty()) {
            val schedulers = schedulerService.findByFetchCount(this.task.fetchCount, schedulerId, this.task.id, 100)
            schedulers.forEach {
                val request = Request(it.url)
                request.putExtra("extractResult", it.extractResult)
                queue.offer(request)
                schedulerId = it.id!!
            }
        }
        return queue.poll()
    }


    override fun push(request: Request, task: Task) {
        val extractResult = request.extras.get("extractResult")
        val scheduler = Scheduler(null, this.task.id, request.url, false, 0, Convert.toBool(extractResult, false),
                LocalDateTime.now(), LocalDateTime.now())
        schedulerService.save(scheduler)
    }


    override fun getLeftRequestsCount(task: Task): Int {
        val findCount = schedulerService.findCountByFetchCount(this.task.fetchCount, this.task.id)
        return findCount
    }

    override fun getTotalRequestsCount(task: Task): Int {
        val findCount = schedulerService.findCount(this.task.id)
        return findCount
    }

    override fun close() {


    }

}