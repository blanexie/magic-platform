package com.github.blanexie.magic.platform

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


/**
 *
 * @author xiezc
 * @date 2024/8/16 17:55
 */
@MapperScan(basePackages = ["com.github.blanexie.magic.platform.mapper"])
@SpringBootApplication
class App {



}


fun main(args: Array<String>) {
    try {
        runApplication<App>(*args)
    }catch (e: Exception){
        e.printStackTrace()
    }
}
