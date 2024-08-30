package com.github.blanexie.magic.platform.common

import cn.hutool.core.convert.Convert
import com.github.blanexie.magic.platform.entity.Spider
import us.codecraft.webmagic.Page
import us.codecraft.webmagic.Request
import us.codecraft.webmagic.Site
import us.codecraft.webmagic.configurable.ExtractRule
import us.codecraft.webmagic.processor.PageProcessor


/**
 *
 * @author xiezc
 * @date 2024/8/29 17:21
 */
class MagicPageProcessor(val spider: Spider) : PageProcessor {

    private val site: Site

    init {
        val retryTimes = Convert.toInt(spider.other["retryTimes"])
        val sleepTime = Convert.toInt(spider.other["sleepTime"])
        site = Site.me().setRetryTimes(retryTimes).setSleepTime(sleepTime).setUseGzip(true)
    }

    override fun getSite(): Site {
        return site
    }

    override fun process(page: Page) {
        val schedulerId = page.request.getExtra<Int>("schedulerId")
        page.resultItems.put("schedulerId", schedulerId)
        val extractResult = page.request.getExtra<Boolean>("extractResult")
        page.resultItems.put("extractResult", extractResult)
        page.resultItems.put("requestUrl", page.request.url)
        page.resultItems.put("fetchCount", spider.fetchCount)


        for (extractRule in spider.helpUrls) {
            extractedResult(extractRule, page, false)
        }

        for (extractRule in spider.targetUrls) {
            extractedResult(extractRule, page, true)
        }

        for (extractRule in spider.extracts) {
            if (extractRule.isMulti) {
                val results = page.html.selectDocumentForList(extractRule.selector)
                if (extractRule.isNotNull && results.size == 0) {
                    page.setSkip(true)
                } else {
                    page.resultItems.put(extractRule.fieldName, results)
                }
            } else {
                val result = page.html.selectDocument(extractRule.selector)
                if (extractRule.isNotNull && result == null) {
                    page.setSkip(true)
                } else {
                    page.resultItems.put(extractRule.fieldName, result)
                }
            }
        }
    }

    private fun extractedResult(extractRule: ExtractRule, page: Page, extractResult: Boolean) {
        if (extractRule.isMulti) {
            val results = page.html.selectDocumentForList(extractRule.selector)
            if (extractRule.isNotNull && results.size == 0) {
                page.setSkip(true)
            } else {
                results.forEach {
                    val request = Request(it)
                    request.putExtra("extractResult", extractResult)
                    page.addTargetRequest(request)
                }
            }
        } else {
            val result = page.html.selectDocument(extractRule.selector)
            if (extractRule.isNotNull && result == null) {
                page.setSkip(true)
            } else {
                val request = Request(result)
                request.putExtra("extractResult", extractResult)
                page.addTargetRequest(request)
            }
        }
    }
}