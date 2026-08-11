package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_device")
@Schema(description = "智能监测-设备管理")
public class IotDevice extends BaseEntity {
    @Schema(description = "deviceName")    private String deviceName;
    @Schema(description = "deviceNo")    private String deviceNo;
    @Schema(description = "deviceType")    private String deviceType;
    @Schema(description = "location")    private String location;
    @Schema(description = "status")    private Integer status;
}
