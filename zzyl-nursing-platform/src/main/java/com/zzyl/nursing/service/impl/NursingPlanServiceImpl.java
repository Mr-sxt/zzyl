package com.zzyl.nursing.service.impl;

import java.util.List;
import com.zzyl.common.utils.DateUtils;
import com.zzyl.common.utils.bean.BeanUtils;
import com.zzyl.nursing.domain.NursingProjectPlan;
import com.zzyl.nursing.dto.NursingPlanDto;
import com.zzyl.nursing.mapper.NursingProjectPlanMapper;
import com.zzyl.nursing.vo.NursingPlanVo;
import com.zzyl.nursing.vo.NursingProjectPlanVo;
import com.zzyl.nursing.vo.NursingProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzyl.nursing.mapper.NursingPlanMapper;
import com.zzyl.nursing.domain.NursingPlan;
import com.zzyl.nursing.service.INursingPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 护理计划Service业务层处理
 * 
 * @author feel
 * @date 2025-10-09
 */
@Service
public class NursingPlanServiceImpl extends ServiceImpl<NursingPlanMapper, NursingPlan> implements INursingPlanService
{
    @Autowired
    private NursingPlanMapper nursingPlanMapper;

    @Autowired
    private NursingProjectPlanMapper nursingProjectPlanMapper;

    /**
     * 查询护理计划
     * 
     * @param id 护理计划主键
     * @return 护理计划
     */
    @Override
    public NursingPlanVo selectNursingPlanById(Long id)
    {
        //查询护理计划基本信息
        NursingPlan nursingPlan = nursingPlanMapper.selectById(id);
        //查询关联的集合
        List<NursingProjectPlanVo> projectPlans = nursingProjectPlanMapper.selectByPlanId(id);
        //合并对象
        NursingPlanVo nursingPlanVo = new NursingPlanVo();
        BeanUtils.copyProperties(nursingPlan,nursingPlanVo);
        nursingPlanVo.setProjectPlans(projectPlans);
        return nursingPlanVo;
    }

    /**
     * 查询护理计划列表
     * 
     * @param nursingPlan 护理计划
     * @return 护理计划
     */
    @Override
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan)
    {
        return nursingPlanMapper.selectNursingPlanList(nursingPlan);
    }

    /**
     * 新增护理计划
     *
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertNursingPlan(NursingPlanDto dto)
    {
        //保存护理计划
        NursingPlan nursingPlan = new NursingPlan();
        BeanUtils.copyProperties(dto,nursingPlan);
        nursingPlan.setCreateTime(DateUtils.getNowDate());
        nursingPlanMapper.insertNursingPlan(nursingPlan);

        //保存关系
        int cont = nursingProjectPlanMapper.batchInsert(dto.getProjectPlans(), nursingPlan.getId());
        return cont == 0 ? 0 : 1;
    }

    /**
     * 修改护理计划
     * 
     * @param dto 护理计划
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateNursingPlan(NursingPlanDto dto)
    {
        //判断护理项目是否为空
        if(dto.getProjectPlans() != null && !dto.getProjectPlans().isEmpty()){
            //删除护理计划对应的护理列表
            nursingProjectPlanMapper.deleteByNursingPlanId(dto.getId());
            //批量保存护理项目
            nursingProjectPlanMapper.batchInsert(dto.getProjectPlans(), dto.getId());
        }

        //项目为空直接进行保存
        NursingPlan nursingPlan = new NursingPlan();
        BeanUtils.copyProperties(dto,nursingPlan);
        //修改护理计划
        return nursingPlanMapper.updateNursingPlan(nursingPlan);
    }

    /**
     * 批量删除护理计划
     * 
     * @param ids 需要删除的护理计划主键
     * @return 结果
     */
    @Override
    public int deleteNursingPlanByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除护理计划信息
     * 
     * @param id 护理计划主键
     * @return 结果
     */
    @Override
    public int deleteNursingPlanById(Long id)
    {
        //删除护理计划关联的护理项目
        nursingProjectPlanMapper.deleteByNursingPlanId(id);
        //删除护理计划
        return removeById(id)? 1 : 0;
    }

    @Override
    public List<NursingPlan> getAllNursingPlans() {
        return nursingPlanMapper.getAllNursingPlans();
    }
}
