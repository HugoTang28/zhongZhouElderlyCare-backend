package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_refund")
@Schema(description = "订单-退款管理")
public class OrderRefund extends BaseEntity {
    @Schema(description = "orderNo")    private String orderNo;
    @Schema(description = "refundNo")    private String refundNo;
    @Schema(description = "amount")    private BigDecimal amount;
    @Schema(description = "status")    private Integer status;
}
