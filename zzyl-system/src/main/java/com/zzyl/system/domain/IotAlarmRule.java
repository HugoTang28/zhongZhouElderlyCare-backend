package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_alarm_rule")
@Schema(description = "智能监测-报警规则")
public class IotAlarmRule extends BaseEntity {
    @Schema(description = "ruleName")    private String ruleName;
    @Schema(description = "deviceType")    private String deviceType;
    @Schema(description = "alarmCondition")    private String alarmCondition;
    @Schema(description = "threshold")    private String threshold;
    @Schema(description = "alarmLevel")    private Integer alarmLevel;
    @Schema(description = "status")    private Integer status;
}
