package com.bigData.service.system.service.impl;

import com.bigData.common.base.BaseServiceImpl;
import com.bigData.common.core.DataUser;
import com.bigData.common.utils.IPUtil;
import com.bigData.common.utils.SecurityUtil;
import com.bigData.service.system.api.entity.LoginLogEntity;
import com.bigData.service.system.dao.LoginLogDao;
import com.bigData.service.system.mapstruct.LoginLogMapper;
import com.bigData.service.system.service.LoginLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 登录日志信息表 服务实现类
 * </p>
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLogDao, LoginLogEntity> implements LoginLogService {

    @Autowired
    private LoginLogDao loginLogDao;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLoginLog(HttpServletRequest request) {
        String ip = IPUtil.getIpAddr(request);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String os = userAgent.getOperatingSystem().getName();
        String browser = userAgent.getBrowser().getName();
        DataUser user = SecurityUtil.getDataUser();
        String userId = user.getId();
        String username = user.getUsername();
        LoginLogEntity loginLog = new LoginLogEntity();
        loginLog.setOpIp(ip).setOpOs(os).setOpBrowser(browser).setUserId(userId).setUserName(username).setOpDate(LocalDateTime.now());
        loginLogDao.insert(loginLog);
    }

    @Override
    public LoginLogEntity getLoginLogById(String id) {
        LoginLogEntity loginLogEntity = super.getById(id);
        return loginLogEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLoginLogById(String id) {
        loginLogDao.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLoginLogBatch(List<String> ids) {
        loginLogDao.deleteBatchIds(ids);
    }
}
