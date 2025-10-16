package com.zzyl.nursing.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzyl.common.exception.base.BaseException;
import com.zzyl.nursing.domain.*;
import com.zzyl.nursing.dto.CheckInApplyDto;
import com.zzyl.nursing.dto.CheckInConfigDto;
import com.zzyl.nursing.dto.CheckInElderDto;
import com.zzyl.nursing.mapper.*;
import com.zzyl.nursing.vo.CheckInConfigVo;
import com.zzyl.nursing.vo.CheckInDetailVo;
import com.zzyl.nursing.vo.CheckInElderVo;
import com.zzyl.nursing.vo.ElderFamilyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzyl.nursing.service.ICheckInService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import static com.zzyl.common.utils.CodeGenerator.generateContractNumber;

/**
 * 入住Service业务层处理
 * 
 * @author alexis
 * @date 2025-10-13
 */
@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements ICheckInService
{
    @Autowired
    private CheckInMapper checkInMapper;
    @Autowired
    private ElderMapper edMapper;
    @Autowired
    private BedMapper bedMapper;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private CheckInConfigMapper checkInConfigMapper;


    /**
     * 查询入住
     * 
     * @param id 入住主键
     * @return 入住
     */
    @Override
    public CheckIn selectCheckInById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询入住列表
     * 
     * @param checkIn 入住
     * @return 入住
     */
    @Override
    public List<CheckIn> selectCheckInList(CheckIn checkIn)
    {
        return checkInMapper.selectCheckInList(checkIn);
    }

    /**
     * 新增入住
     * 
     * @param checkIn 入住
     * @return 结果
     */
    @Override
    public int insertCheckIn(CheckIn checkIn)
    {
        return save(checkIn) ? 1 : 0;
    }

    /**
     * 修改入住
     * 
     * @param checkIn 入住
     * @return 结果
     */
    @Override
    public int updateCheckIn(CheckIn checkIn)
    {
        return updateById(checkIn) ? 1 : 0;
    }

    /**
     * 批量删除入住
     * 
     * @param ids 需要删除的入住主键
     * @return 结果
     */
    @Override
    public int deleteCheckInByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除入住信息
     * 
     * @param id 入住主键
     * @return 结果
     */
    @Override
    public int deleteCheckInById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }


    /**
     * 入住申请
     * @param checkIn
     */
    @Override
    public void apply(CheckInApplyDto checkIn) {
        //检验老人是否入住
        LambdaQueryWrapper<Elder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Elder::getIdCardNo,checkIn.getCheckInElderDto().getIdCardNo())
                .in(Elder::getStatus,1,4);
        Elder elder = edMapper.selectOne(queryWrapper);
        if(ObjectUtil.isNotEmpty(elder)){
            throw new BaseException("该老人已入住");
        }
        //更新床位状态为已经入住
        Bed bed = bedMapper.selectById(checkIn.getCheckInConfigDto().getBedId());
        bed.setBedStatus(1);
        bedMapper.updateById(bed);
        //更新老人信息
        elder = insertOrUpdateElder(bed,checkIn.getCheckInElderDto());
        //生成合同编号
        String contractNumber ="HT" + generateContractNumber();
        //新增签约办理
        insertContract(elder,contractNumber,checkIn);
        //新增入住信息
        CheckIn checkInInfo = insertCheckInInfo(elder,checkIn);
        //新增入住配置
        insertCheckInConfig(checkInInfo.getId(),checkIn.getCheckInConfigDto());
    }

    /**
     * 新增入住配置
     * @param id
     * @param checkInConfigDto
     */
    private void insertCheckInConfig(Long id, CheckInConfigDto checkInConfigDto) {
        CheckInConfig checkInConfig = new CheckInConfig();
        checkInConfig.setCheckInId(id);
        //属性拷贝
        BeanUtils.copyProperties(checkInConfigDto,checkInConfig);
        checkInConfigMapper.insert(checkInConfig);
    }

    /**
     * 新增入住信息
     * @param elder
     * @param checkIn
     */
    private CheckIn insertCheckInInfo(Elder elder, CheckInApplyDto checkIn) {
        CheckIn check = new CheckIn();
        check.setElderId(elder.getId());
        check.setElderName(elder.getName());
        check.setIdCardNo(elder.getIdCardNo());
        check.setStartDate(checkIn.getCheckInConfigDto().getStartDate());
        check.setEndDate(checkIn.getCheckInConfigDto().getEndDate());
        check.setNursingLevelName(checkIn.getCheckInConfigDto().getNursingLevelName());
        check.setBedNumber(elder.getBedNumber());
        check.setStatus(0);
        check.setRemark(JSON.toJSONString(checkIn.getElderFamilyDtoList()));
        checkInMapper.insert(check);
        return check;
    }

    /**
     * 新增签约办理
     * @param elder
     * @param contractNumber
     * @param checkIn
     */
    private void insertContract(Elder elder, String contractNumber, CheckInApplyDto checkIn) {
        //设置老人信息
        Contract contract = new Contract();
        BeanUtils.copyProperties(checkIn.getCheckInContractDto(),contract);
        contract.setContractNumber(contractNumber);
        contract.setElderId(elder.getId());
        contract.setElderName(elder.getName());
        contract.setStartDate(checkIn.getCheckInConfigDto().getStartDate());
        contract.setEndDate(checkIn.getCheckInConfigDto().getEndDate());
        //设置状态
        int status = checkIn.getCheckInConfigDto().getStartDate().isAfter(LocalDateTime.now()) ? 0 : 1;
        contract.setStatus(status);
        contractMapper.insert(contract);
    }

    /**
     * 新增或修改老人信息
     * @param bed
     * @param checkInElderDto
     */
    private Elder insertOrUpdateElder(Bed bed, CheckInElderDto checkInElderDto) {
        Elder elder = new Elder();
        //属性拷贝
        BeanUtils.copyProperties(checkInElderDto,elder);
        elder.setBedId(bed.getId());
        elder.setBedNumber(bed.getBedNumber());
        elder.setStatus(1);
        //判断老人信息
        LambdaQueryWrapper<Elder> lam = new LambdaQueryWrapper<>();
        lam.eq(Elder::getIdCardNo,elder.getIdCardNo());
        lam.notIn(Elder::getStatus,1,4);
        Elder elderInDb = edMapper.selectOne(lam);
        if(ObjectUtil.isNotEmpty(elderInDb)){
            //修改
            elder.setId(elderInDb.getId());
            edMapper.updateById(elder);
        }else{
            //新增
            edMapper.insert(elder);
        }
        return elder;
    }

    /**
     * 查询入住详情
     * @param id
     * @return
     */
    @Override
    public CheckInDetailVo detail(Long id) {
        CheckInDetailVo detailVo = new CheckInDetailVo();
        //获取入住信息
        CheckInConfigVo checkIn =  new CheckInConfigVo();
        CheckIn check = checkInMapper.selectById(id);
        BeanUtils.copyProperties(check,checkIn);

        CheckInConfig checkInConfig = checkInConfigMapper.selectOne(new LambdaQueryWrapper<CheckInConfig>().eq(CheckInConfig::getCheckInId,id));
        BeanUtils.copyProperties(checkInConfig,checkIn);

        detailVo.setCheckInConfigVo(checkIn);

        //获取老人信息
        CheckInElderVo checkInElderVo = new CheckInElderVo();
        //获取老人id
        Long elderId = check.getElderId();
        Elder elder = edMapper.selectById(elderId);
        BeanUtils.copyProperties(elder,checkInElderVo);
        //从身份证中获取老人的年龄
        checkInElderVo.setAge(IdcardUtil.getAgeByIdCard(elder.getIdCardNo()));
        detailVo.setCheckInElderVo(checkInElderVo);


        //设置家属响应信息
        String remark = check.getRemark();
        List<ElderFamilyVo> familyList = JSON.parseArray(remark, ElderFamilyVo.class);
        detailVo.setElderFamilyVoList(familyList);

        //设置签约办理响应信息
        Contract contract = contractMapper.selectOne(new LambdaQueryWrapper<Contract>().eq(Contract::getElderId,elderId));
        detailVo.setContract(contract);
        //返回结果
        return detailVo;
    }
}
