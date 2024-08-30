package com.github.blanexie.magic.platform.entity

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler
import us.codecraft.webmagic.configurable.ExtractRule
import us.codecraft.webmagic.model.annotation.HelpUrl
import java.time.LocalDateTime
import javax.print.attribute.standard.MediaSize.Other


/**
 *
 * @author xiezc
 * @date 2024/8/26 19:19
 */
data class Spider(
        val id: Int,
        var name: String,

        var fetchCount: Int,

        @TableField(typeHandler = FastjsonTypeHandler::class)
        var startUrls: ArrayList<String>, //启动url
        @TableField(typeHandler = FastjsonTypeHandler::class)
        var targetUrls: ArrayList<ExtractRule>,
        @TableField(typeHandler = FastjsonTypeHandler::class)
        var helpUrls: ArrayList<ExtractRule>,
        @TableField(typeHandler = FastjsonTypeHandler::class)
        var extracts: ArrayList<ExtractRule>, //抓取规则， json格式


        var status: Int,   // 0: 待执行， 1：执行中， 2：已结束，   3：暂停中   4：等待下次定时启动中

        var cron: String,  // 定时任务， 到了时间，会把任务状态重新设值成执行中

        //定时模式;  clean: 会清除调scheduler, 和 result 中的内容， 重新开始任务
        // mergeResult: 只会清除scheduler， 但是result会合并
        // merget: scheduler和result都不会清除，只会从开始链接，重新开始任务。 注意会调用scheduler去重， 但是中间页面url不会参与去重
        var cronType: String,

        var cookie: String,
        @TableField(typeHandler = FastjsonTypeHandler::class)
        var headers: HashMap<String, String>,

        @TableField(typeHandler = FastjsonTypeHandler::class)
        var other: HashMap<String, String>,

        @TableField(fill = FieldFill.INSERT_UPDATE)
        var updateTime: LocalDateTime,
        @TableField(fill = FieldFill.INSERT)
        var createTime: LocalDateTime,
)
