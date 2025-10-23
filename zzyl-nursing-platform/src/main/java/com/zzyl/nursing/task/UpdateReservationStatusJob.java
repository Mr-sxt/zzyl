package com.zzyl.nursing.task;

import com.zzyl.nursing.service.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时更新预约状态任务
 *
 * @author alexis
 * @date 2025-10-22
 */
public class UpdateReservationStatusJob {
    @Autowired
    private IReservationService reservationService;

    /**
     * 执行任务
     */
    public void updateReservationStatus() {
        reservationService.updateReservationStatus();
    }
}
