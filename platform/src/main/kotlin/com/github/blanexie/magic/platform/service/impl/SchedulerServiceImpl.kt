package com.github.blanexie.magic.platform.service.impl

import com.baomidou.mybatisplus.core.toolkit.Wrappers
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.github.blanexie.magic.platform.entity.Scheduler
import com.github.blanexie.magic.platform.mapper.SchedulerMapper
import com.github.blanexie.magic.platform.service.SchedulerService
import org.springframework.stereotype.Service


/**
 *
 * @author xiezc
 * @date 2024/8/29 15:38
 */
@Service
class SchedulerServiceImpl : SchedulerService, ServiceImpl<SchedulerMapper, Scheduler>() {

    override fun findByFetchCount(fetchCount: Int, startId: Int, spiderId: Int, size: Int): MutableList<Scheduler> {
        val wrapper = Wrappers.lambdaQuery(Scheduler::class.java)
                .eq(Scheduler::fetchCount, fetchCount)
                .eq(Scheduler::spiderId, spiderId)
                .ge(Scheduler::id, startId)
                .last("limit ${size}")

        val selectList = this.baseMapper.selectList(wrapper)
        return selectList
    }

    override fun findCountByFetchCount(fetchCount: Int, spiderId: Int): Int {
        val wrapper = Wrappers.lambdaQuery(Scheduler::class.java)
                .eq(Scheduler::fetchCount, fetchCount)
                .eq(Scheduler::spiderId, spiderId)
        return this.baseMapper.selectCount(wrapper).toInt()
    }

    override fun findCount(spiderId: Int): Int {
        val wrapper = Wrappers.lambdaQuery(Scheduler::class.java)
                .eq(Scheduler::spiderId, spiderId)
        return this.baseMapper.selectCount(wrapper).toInt()
    }

    override fun updateFetchCount(fetchCount: Int, schedulerId: Int) {
        val updateWrapper = Wrappers.lambdaUpdate(Scheduler::class.java)
                .eq(Scheduler::id, schedulerId)
                .set(Scheduler::fetchCount, fetchCount)
        this.update(updateWrapper)
    }

}