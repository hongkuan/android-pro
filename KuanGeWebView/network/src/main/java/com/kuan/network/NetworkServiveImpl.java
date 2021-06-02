package com.kuan.network;

import com.google.auto.service.AutoService;
import com.kuan.common.autoservice.INetwrokService;

@AutoService(INetwrokService.class)
public class NetworkServiveImpl implements INetwrokService {
    @Override
    public void getNewChannels() {
        NetworkApi.getTecentNewsChannels();
    }
}
