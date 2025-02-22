package com.bigData.service.email.service.impl;

import com.bigData.common.base.BaseServiceImpl;
import com.bigData.common.utils.ThrowableUtil;
import com.bigData.service.email.api.dto.EmailDto;
import com.bigData.service.email.api.entity.EmailEntity;
import com.bigData.service.email.dao.EmailDao;
import com.bigData.service.email.mapstruct.EmailMapper;
import com.bigData.service.email.service.EmailService;
import com.bigData.service.email.utils.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmailServiceImpl extends BaseServiceImpl<EmailDao, EmailEntity> implements EmailService {

    @Autowired
    private EmailDao emailDao;

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    public EmailEntity getEmailById(String id) {
        EmailEntity emailEntity = super.getById(id);
        return emailEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEmail(EmailDto emailDto) {
        EmailEntity emailEntity = emailMapper.toEntity(emailDto);
        emailDao.insert(emailEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(EmailDto emailDto) {
        EmailEntity emailEntity = emailMapper.toEntity(emailDto);
        emailDao.updateById(emailEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEmailById(String id) {
        emailDao.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEmailBatch(List<String> ids) {
        emailDao.deleteBatchIds(ids);
    }

    @Override
    public void sendEmail(String id) {
        EmailEntity emailEntity = super.getById(id);
        try {
            emailUtil.sendEmail(emailEntity);
        } catch (Exception e) {
            log.error("全局异常信息ex={}, StackTrace={}", e.getMessage(), ThrowableUtil.getStackTrace(e));
        }
    }
}
