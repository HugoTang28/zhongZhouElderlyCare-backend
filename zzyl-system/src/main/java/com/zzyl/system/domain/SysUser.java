package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Schema(description = "系统用户")
public class SysUser extends BaseEntity {

    @Schema(description = "登录账号")
    private String username;

    // WRITE_ONLY：仅接收前端传参（新增/改密），序列化响应时不外泄密码
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码")
    private String password;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "性别：0男 1女")
    private Integer sex;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "状态：0正常 1停用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @TableField(exist = false)
    @Schema(description = "角色ID集合（表单提交用，非数据库字段）")
    private Long[] roleIds;
}
