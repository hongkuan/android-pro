package com.kuan.usercenter;

import android.content.Intent;

import com.google.auto.service.AutoService;
import com.kuan.base.BaseApplication;
import com.kuan.common.autoservice.IUserCenterService;

/**
 * Created by hongkuan on 2021-05-12 0012.
 */
@AutoService(IUserCenterService.class)
public class UserCenterServiceImpl implements IUserCenterService {
    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public void login() {
        Intent intent = new Intent(BaseApplication.sApplication, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.sApplication.startActivity(intent);
    }
}
