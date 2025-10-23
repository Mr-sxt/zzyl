package com.zzyl.nursing.service;

import java.util.List;

import com.zzyl.common.core.domain.AjaxResult;
import com.zzyl.common.core.page.TableDataInfo;
import com.zzyl.nursing.domain.Reservation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.nursing.dto.ReservationDto;
import com.zzyl.nursing.vo.member.TimeCountVo;

/**
 * 预约信息Service接口
 * 
 * @author alexis
 * @date 2025-10-22
 */
public interface IReservationService extends IService<Reservation>
{
    /**
     * 查询预约信息
     * 
     * @param id 预约信息主键
     * @return 预约信息
     */
    public Reservation selectReservationById(Long id);

    /**
     * 查询预约信息列表
     * 
     * @param reservation 预约信息
     * @return 预约信息集合
     */
    public List<Reservation> selectReservationList(Reservation reservation);

    /**
     * 新增预约信息
     * 
     * @param reservation 预约信息
     * @return 结果
     */
    public int insertReservation(Reservation reservation);

    /**
     * 修改预约信息
     * 
     * @param reservation 预约信息
     * @return 结果
     */
    public int updateReservation(Reservation reservation);

    /**
     * 批量删除预约信息
     * 
     * @param ids 需要删除的预约信息主键集合
     * @return 结果
     */
    public int deleteReservationByIds(Long[] ids);

    /**
     * 删除预约信息信息
     * 
     * @param id 预约信息主键
     * @return 结果
     */
    public int deleteReservationById(Long id);

    /**
     * 查询取消预约数量
     * @param userId
     * @return
     */
    int getCancelledCount(Long userId);


    /**
     * 查询每个时间段的剩余次数
     *
     * @param time
     * @return
     */
    List<TimeCountVo> countReservationsForTime(Long time);

    /**
     * 添加预约信息
     * @param reservationDto
     * @return
     */
    int insertReservationDto(ReservationDto reservationDto);

    /**
     * 分页查询参数预约
     */
    AjaxResult pageByNameAndStaus(Integer pageSize, Integer pageNum, Integer status);

    /**
     * 定时更新预约状态
     */
    void updateReservationStatus();
}
