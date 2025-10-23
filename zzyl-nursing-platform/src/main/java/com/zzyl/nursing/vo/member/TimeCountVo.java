package com.zzyl.nursing.vo.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("分时段统计vo")
public class TimeCountVo {
    @ApiModelProperty(value = "时间段")
    private String time;

    @ApiModelProperty(value = "剩余数量")
    private String count;
}
