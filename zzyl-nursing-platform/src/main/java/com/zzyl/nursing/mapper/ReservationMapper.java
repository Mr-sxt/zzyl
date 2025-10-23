package com.zzyl.nursing.mapper;

import java.time.LocalDateTime;
import java.util.List;
import com.zzyl.nursing.domain.Reservation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzyl.nursing.dto.ReservationDto;
import com.zzyl.nursing.vo.member.TimeCountVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预约信息Mapper接口
 * 
 * @author alexis
 * @date 2025-10-22
 */
@Mapper
public interface ReservationMapper extends BaseMapper<Reservation>
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
     * 删除预约信息
     * 
     * @param id 预约信息主键
     * @return 结果
     */
    public int deleteReservationById(Long id);

    /**
     * 批量删除预约信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteReservationByIds(Long[] ids);

    /**
     * 查询每个时间段的剩余次数
     *
     * @param
     * @return
     */
    List<TimeCountVo> countReservationsForTime(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 添加预约信息
     * @param reservationDto
     * @return
     */
    @Insert("insert into reservation(mobile,name,time,type,visitor,status) values(#{mobile},#{name},#{time},#{type},#{visitor},0)")
    int insertReservationDto(ReservationDto reservationDto);
}
