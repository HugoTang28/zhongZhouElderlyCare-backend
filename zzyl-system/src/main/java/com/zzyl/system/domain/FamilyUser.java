package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 家属端-家属用户
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("family_user")
@Schema(description = "家属端-家属用户")
public class FamilyUser extends BaseEntity {

    @Schema(description = "手机号（登录账号，关联 checkin_admission.family_phone）")
    private String phone;

    @Schema(description = "微信 openid（预留，接微信登录时填充）")
    private String openid;

    @Schema(description = "密码（BCrypt，验证码登录可为空）")
    private String password;

    @Schema(description = "状态 0正常 1停用")
    private Integer status;
}
