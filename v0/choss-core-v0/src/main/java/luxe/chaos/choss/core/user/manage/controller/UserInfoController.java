package luxe.chaos.choss.core.user.manage.controller;

import luxe.chaos.choss.core.user.manage.dao.UserInfoMapper;
import luxe.chaos.choss.core.user.manage.model.UserInfo;
import luxe.chaos.choss.core.user.manage.service.UserInfoService;
import luxe.chaos.choss.core.user.manage.service.impl.UserInfoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 12:49 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@RestController
public class UserInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

    private UserInfoService userInfoService;

    @PostMapping("/v1/add-user")
    public Map<String, Object> addUser(UserInfo userInfo) {

        LOGGER.info("user info => {}", userInfo);


        this.userInfoService.addUser(userInfo);

        return Collections.singletonMap("message", "SUCCESS");
    }
}
