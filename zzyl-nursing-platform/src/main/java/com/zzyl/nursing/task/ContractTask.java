package com.zzyl.nursing.task;

import com.zzyl.nursing.service.IContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContractTask {
    @Autowired
    private IContractService contractService;
    public void updateContractStatus(){
        log.info("开始执行合同状态更新任务");
        contractService.updateContractStatus();
        log.info("结束执行合同状态更新任务");
    }
}
