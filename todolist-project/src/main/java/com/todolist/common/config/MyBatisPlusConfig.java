package com.todolist.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus Configuration
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * MyBatis-Plus Interceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // Pagination interceptor
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInterceptor.setMaxLimit(100L); // Maximum single page limit
        paginationInterceptor.setOverflow(false); // Overflow to home page
        interceptor.addInnerInterceptor(paginationInterceptor);

        // Optimistic locker interceptor
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // Prevent full table update/delete interceptor
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        return interceptor;
    }
}
