package com.zzyl.nursing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "预约人")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预约时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "预约类型")
    private Integer type;

    @ApiModelProperty(value = "家人姓名")
    private String visitor;

}
