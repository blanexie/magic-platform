package com.github.blanexie.magic.platform.service.impl

import com.github.blanexie.magic.platform.entity.Scheduler
import com.github.blanexie.magic.platform.mapper.SchedulerRepository
import com.github.blanexie.magic.platform.service.SchedulerService
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


/**
 *
 * @author xiezc
 * @date 2024/8/29 15:38
 */
@Service
class SchedulerServiceImpl(
        val schedulerRepository: SchedulerRepository
) : SchedulerService {

    override fun findByFetchCount(fetchCount: Int, startId: Int, spiderId: Int, size: Int): MutableList<Scheduler> {
        return schedulerRepository.findAllBySpiderIdAndFetchCountAndIdAfter(spiderId, fetchCount, startId, Pageable.ofSize(size))
    }

    override fun findCountByFetchCount(fetchCount: Int, spiderId: Int): Int {
        return schedulerRepository.countBySpiderIdAndFetchCount(spiderId, fetchCount)
    }

    override fun findCount(spiderId: Int): Int {
        return schedulerRepository.countBySpiderId(spiderId)
    }

    override fun updateFetchCount(fetchCount: Int, schedulerId: Int) {
        schedulerRepository.findByIdOrNull(schedulerId)?.let {
            it.fetchCount = fetchCount
            schedulerRepository.save(it)
        }
    }

    override fun save(scheduler: Scheduler) {
        schedulerRepository.save(scheduler)
    }

}