package com.zzyl.admin.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 家属端登录入参
 */
@Data
public class FamilyLoginBody {

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "短信验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
