package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("checkin_checkout")
@Schema(description = "入退-退住管理")
public class CheckinCheckout extends BaseEntity {
    @Schema(description = "elderName")    private String elderName;
    @Schema(description = "bedId")    private Long bedId;
    @Schema(description = "checkOutTime")    private LocalDateTime checkOutTime;
    @Schema(description = "reason")    private String reason;
    @Schema(description = "refundAmount")    private BigDecimal refundAmount;
    @Schema(description = "status")    private Integer status;
}
