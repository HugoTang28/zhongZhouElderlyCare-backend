package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("finance_prestore")
@Schema(description = "财务-预存管理")
public class FinancePrestore extends BaseEntity {
    @Schema(description = "elderName")    private String elderName;
    @Schema(description = "amount")    private BigDecimal amount;
    @Schema(description = "balance")    private BigDecimal balance;
    @Schema(description = "transType")    private Integer transType;
}
