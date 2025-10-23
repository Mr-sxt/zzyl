package com.zzyl.nursing.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzyl.common.core.domain.AjaxResult;
import com.zzyl.common.core.page.TableDataInfo;
import com.zzyl.common.utils.UserThreadLocal;
import com.zzyl.common.utils.bean.BeanUtils;
import com.zzyl.nursing.dto.ReservationDto;
import com.zzyl.nursing.vo.member.TimeCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzyl.nursing.mapper.ReservationMapper;
import com.zzyl.nursing.domain.Reservation;
import com.zzyl.nursing.service.IReservationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 预约信息Service业务层处理
 * 
 * @author alexis
 * @date 2025-10-22
 */
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements IReservationService
{
    @Autowired
    private ReservationMapper reservationMapper;

    /**
     * 查询预约信息
     * 
     * @param id 预约信息主键
     * @return 预约信息
     */
    @Override
    public Reservation selectReservationById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询预约信息列表
     * 
     * @param reservation 预约信息
     * @return 预约信息
     */
    @Override
    public List<Reservation> selectReservationList(Reservation reservation)
    {
        return reservationMapper.selectReservationList(reservation);
    }

    /**
     * 新增预约信息
     * 
     * @param reservation 预约信息
     * @return 结果
     */
    @Override
    public int insertReservation(Reservation reservation)
    {
        return save(reservation) ? 1 : 0;
    }

    /**
     * 修改预约信息
     * 
     * @param reservation 预约信息
     * @return 结果
     */
    @Override
    public int updateReservation(Reservation reservation)
    {
        return updateById(reservation) ? 1 : 0;
    }

    /**
     * 批量删除预约信息
     * 
     * @param ids 需要删除的预约信息主键
     * @return 结果
     */
    @Override
    public int deleteReservationByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除预约信息信息
     * 
     * @param id 预约信息主键
     * @return 结果
     */
    @Override
    public int deleteReservationById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }

    /**
     * 查询取消预约数量
     *
     * @param userId
     * @return
     */
    @Override
    public int getCancelledCount(Long userId) {
        LocalDateTime startTime = LocalDate.now().atStartOfDay();
        LocalDateTime endTime = startTime.plusDays(1);
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reservation::getUpdateBy, userId)
                .eq(Reservation::getStatus, 2)
                .between(Reservation::getUpdateTime, startTime, endTime);
        return (int) count(queryWrapper);
    }

    /**
     * 分页查询参数预约
     *
     * @param pageNum
     * @param pageSize
     * @param status
     */
    @Override
    public AjaxResult pageByNameAndStaus(Integer pageSize, Integer pageNum, Integer status) {
        Long userId = UserThreadLocal.getUserId();
        Page<Reservation> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Reservation::getCreateBy, userId)
                    .eq(status != null ,Reservation::getStatus, status);

        page = page(page,queryWrapper);
        TableDataInfo<Reservation> tableDataInfo = builderTableData(page);

        return AjaxResult.success(tableDataInfo);
    }

    /**
     * 定时更新预约状态
     */
    @Override
    public void updateReservationStatus() {
        //查询预约时间小于当前时间减去30分钟并且状态为0-待报到的预约
        List<Reservation> reservations = reservationMapper.selectList(Wrappers.<Reservation>lambdaQuery()
                .lt(Reservation::getTime,LocalDateTime.now().minusMinutes(30))
                .eq(Reservation::getStatus, 0));

        //设置过期状态
        reservations.forEach(reservation -> reservation.setStatus(3));
        //批量更新
        updateBatchById(reservations);
    }

    public TableDataInfo<Reservation> builderTableData(Page<Reservation> page){
        TableDataInfo<Reservation> tableDataInfo = new TableDataInfo<>();
        tableDataInfo.setRows(page.getRecords());
        tableDataInfo.setTotal(page.getTotal());
        tableDataInfo.setCode(200);
        tableDataInfo.setMsg("查询成功");
        return tableDataInfo;
    }

    /**
     * 添加预约信息
     *
     * @param reservationDto
     * @return
     */
    @Override
    public int insertReservationDto(ReservationDto reservationDto) {
        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(reservationDto, reservation);
        reservation.setStatus(0);
        return reservationMapper.insert(reservation);
    }

    /**
     * 查询每个时间段的剩余次数
     *
     * @param time
     * @return
     */
    public List<TimeCountVo> countReservationsForTime(Long time){
        LocalDateTime now = LocalDateTimeUtil.of(time);
        LocalDateTime startTime = now.toLocalDate().atStartOfDay();
        LocalDateTime endTime = startTime.plusDays(1);

        List<TimeCountVo> timeCountVosLists = reservationMapper.countReservationsForTime(startTime, endTime);
        return timeCountVosLists;
    }
}
