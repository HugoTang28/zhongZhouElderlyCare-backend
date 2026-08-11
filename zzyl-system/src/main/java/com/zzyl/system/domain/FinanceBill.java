package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("finance_bill")
@Schema(description = "财务-账单管理")
public class FinanceBill extends BaseEntity {
    @Schema(description = "billNo")    private String billNo;
    @Schema(description = "elderName")    private String elderName;
    @Schema(description = "billMonth")    private String billMonth;
    @Schema(description = "billType")    private String billType;
    @Schema(description = "amount")    private BigDecimal amount;
    @Schema(description = "status")    private Integer status;
}
