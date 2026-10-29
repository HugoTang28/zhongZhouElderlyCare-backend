package com.zzyl.common.biz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注一个业务模块 Service，使其被通用 CRUD 控制器 {@code BizController} 接管。
 * <ul>
 *   <li>value：模块 key（前端路由/接口使用，如 visitAppointment）</li>
 *   <li>perm：权限前缀（与 sys_menu 中 perms 前缀一致，如 visit:appointment）</li>
 * </ul>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BizModule {
    String value(); // 模块 key

    String perm(); // 权限前缀
}
