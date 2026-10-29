package com.zzyl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 家家乐养老管理系统 - 启动类
 */
@SpringBootApplication
@MapperScan("com.zzyl.**.mapper")
public class ZzylAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZzylAdminApplication.class, args);
        System.out.println("""
                --------------------------------------------------
                  家家乐养老管理系统启动成功
                  接口文档: http://localhost:9995/doc.html
                --------------------------------------------------
                """);
    }
}
