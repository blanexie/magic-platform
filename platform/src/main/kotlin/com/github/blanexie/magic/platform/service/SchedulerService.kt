package com.github.blanexie.magic.platform.service

import com.github.blanexie.magic.platform.entity.Scheduler


/**
 *
 * @author xiezc
 * @date 2024/8/28 14:42
 */
interface SchedulerService {

    fun findByFetchCount(fetchCount: Int, startId: Int, spiderId: Int, size: Int): MutableList<Scheduler>

    fun findCountByFetchCount(fetchCount: Int, spiderId: Int): Int

    fun findCount(spiderId: Int): Int

    fun updateFetchCount(fetchCount: Int, schedulerId: Int)

    fun save(scheduler: Scheduler)

}