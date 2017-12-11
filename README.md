> 由于在android开发中涉及到数据库方面的开发的过程中遇到很多麻烦和困扰,就着手写了个android数据库调试管理工具FastAndr-dbms,希望对大家有点帮助


二话不说,先上图


![图一](http://upload-images.jianshu.io/upload_images/2383936-1acfeed831d9cb93.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![图二](http://upload-images.jianshu.io/upload_images/2383936-6c434257d79ab11f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![图三](http://upload-images.jianshu.io/upload_images/2383936-4bc1424d38f90751.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

界面还是比较小清新....

## 框架功能使用

### 功能

1. 可视化数据编辑界面

2. 可配置服务端口号

3. 可增、删、改、查 数据库及SharedPreference的数据

**缺陷**

1. 不可自定义sqlite语句查询

2. 数据库获取到的boolean型数据只能0或1显示

### 使用

1. gradle添加

```
compile 'cn.hotapk:fastandr_dbms:0.4.0'
```

如果gson冲突

```
 compile ('cn.hotapk:fastandr_dbms:0.4.0'){
        exclude group: 'com.google.code.gson'
    }
```

2. manifest添加网络权限

```
<uses-permission android:name="android.permission.INTERNET" />
```

3. 开启服务

```
 try {
            FDbManager.init(this).startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
```
