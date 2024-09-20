package com.github.blanexie.magic.platform.mapper

import com.github.blanexie.magic.platform.entity.Scheduler
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository


/**
 *
 * @author xiezc
 * @date 2024/8/26 20:15
 */
@Repository
interface SchedulerRepository : JpaRepository<Scheduler, Int>, JpaSpecificationExecutor<Scheduler> {

    fun findAllBySpiderIdAndFetchCountAndIdAfter(spiderId: Int, fetchCount: Int, id: Int, pageable: Pageable): MutableList<Scheduler>

    fun countBySpiderIdAndFetchCount(spiderId: Int, fetchCount: Int): Int

    fun countBySpiderId(spiderId: Int): Int

}