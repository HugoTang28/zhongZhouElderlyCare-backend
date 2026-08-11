package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_info")
@Schema(description = "订单-订单管理")
public class OrderInfo extends BaseEntity {
    @Schema(description = "orderNo")    private String orderNo;
    @Schema(description = "customerName")    private String customerName;
    @Schema(description = "productName")    private String productName;
    @Schema(description = "amount")    private BigDecimal amount;
    @Schema(description = "status")    private Integer status;
}
