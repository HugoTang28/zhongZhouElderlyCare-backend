package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_info")
@Schema(description = "客户管理")
public class CustomerInfo extends BaseEntity {
    @Schema(description = "name")    private String name;
    @Schema(description = "phone")    private String phone;
    @Schema(description = "idCard")    private String idCard;
    @Schema(description = "cusType")    private String cusType;
    @Schema(description = "source")    private String source;
}
