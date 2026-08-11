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
@TableName("resident_contract")
@Schema(description = "在住-合同管理")
public class ResidentContract extends BaseEntity {
    @Schema(description = "contractNo")    private String contractNo;
    @Schema(description = "elderName")    private String elderName;
    @Schema(description = "bedId")    private Long bedId;
    @Schema(description = "startDate")    private LocalDateTime startDate;
    @Schema(description = "endDate")    private LocalDateTime endDate;
    @Schema(description = "monthlyFee")    private BigDecimal monthlyFee;
    @Schema(description = "deposit")    private BigDecimal deposit;
    @Schema(description = "status")    private Integer status;
}
