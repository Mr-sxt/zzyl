package com.zzyl.nursing.controller.member;

import com.zzyl.common.core.controller.BaseController;
import com.zzyl.common.core.domain.AjaxResult;
import com.zzyl.common.core.domain.R;
import com.zzyl.common.core.page.TableDataInfo;
import com.zzyl.common.utils.UserThreadLocal;
import com.zzyl.nursing.domain.Reservation;
import com.zzyl.nursing.dto.ReservationDto;
import com.zzyl.nursing.service.IReservationService;
import com.zzyl.nursing.vo.member.TimeCountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预约信息Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/member/reservation")
@Api(tags =  "预约信息相关接口")
public class MemberReservationController extends BaseController
{
    @Autowired
    private IReservationService reservationService;

    @GetMapping("/cancelled-count")
    @ApiOperation("查询取消预约数量")
    public R<Integer> getCancelledReservationCount() {
        Long userId = UserThreadLocal.getUserId();
        int count = reservationService.getCancelledCount(userId);
        return R.ok(count);
    }

    @GetMapping("/countByTime")
    @ApiOperation("查询每个时间段的剩余次数")
    public R<List<TimeCountVo>> countReservationsForTime(Long time) {
        List<TimeCountVo> timeCountVosLists = reservationService.countReservationsForTime(time);
        return R.ok(timeCountVosLists);
    }

    @PostMapping
    @ApiOperation("新增预约信息")
    public AjaxResult add(@RequestBody ReservationDto reservationDto){
        return toAjax(reservationService.insertReservationDto(reservationDto));
    }

    /**
     * 分页查询预约
     */
    @GetMapping("/page")
    @ApiOperation("分页查询参数预约")
    public AjaxResult getPage(Integer pageSize, Integer pageNum,@RequestParam(required = false) Integer status){
        return reservationService.pageByNameAndStaus(pageSize,pageNum,status);
    }

    /**
     * 取消预约
     */
    @PutMapping("/{id}/cancel")
    @ApiOperation("取消预约")
    public AjaxResult cancel(@PathVariable Long id){
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setStatus(3);
        return toAjax(reservationService.updateReservation(reservation));
    }
}