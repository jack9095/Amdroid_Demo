/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kuanquan.doyincover.publisher.permission;

import android.os.Environment;

import java.io.File;

/**
 * https://github.com/yanzhenjie/AndPermission
 * Created by YanZhenjie on 2018/1/16.
 */
public class StorageReadTest implements PermissionTest {

    public StorageReadTest() {
    }

    @Override
    public boolean test() {
        File directory = Environment.getExternalStorageDirectory();
        if (directory.exists() && directory.canRead()) {
            long modified = directory.lastModified();
            String[] pathList = directory.list();
            return modified > 0 && pathList != null;
        }
        return false;
    }
}