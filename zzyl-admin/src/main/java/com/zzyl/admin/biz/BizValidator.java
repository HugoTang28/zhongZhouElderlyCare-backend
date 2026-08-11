package com.zzyl.admin.biz;

/**
 * 业务模块可选择性实现此接口，在通用 CRUD 保存/更新前做唯一性等业务校验。
 * 校验不通过时抛 {@code com.zzyl.common.core.ServiceException}。
 */
public interface BizValidator {
    /** 新增前校验（entity 为对应模块实体） */
    default void validateBeforeSave(Object entity) {}

    /** 更新前校验（entity 为对应模块实体） */
    default void validateBeforeUpdate(Object entity) {}
}
