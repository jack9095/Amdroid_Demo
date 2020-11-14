
初窥DataStore

一、DataStore 是什么？

    DataStore 提供了一种存储轻量数据的安全稳定的方案，例如配置文件，应用状态等。
    它不支持局部更新：如果任何一个成员变量被修改了，整个对象都将被序列化并持久化到磁盘。
    对于局部修改，请考虑使用 Room。
    
    DataStore 保证原子性，一致性，隔离性，持久性。它是线程安全，且非阻塞的。尤其是，它解决了 SharedPreferences API 的设计缺陷
    官方的博客： https://android-developers.googleblog.com/2020/09/prefer-storing-data-with-jetpack.html
    掘金上的文章：

二、DataStore 与 SharedPreferences 有什么不同，为什么要抛弃 SharedPreferences

    SharedPreferences 目前在使用的过程中存在的问题：
       1). 通过 getXXX() 读取数据会造成主线程阻塞（因为是同步的）
       2). 不能保证类型安全,因为使用相同的 key 进行操作的时候, put 方法可以使用不同类型的数据覆盖掉相同的 key。
       3). apply() 方法是异步的，可能会发生 ANR
    Ps: apply() 方法是异步的，本身是不会有任何问题，但是当生命周期处于  handleStopService() 、 handlePauseActivity() 、
        handleStopActivity()  的时候会一直等待 apply() 方法将数据保存成功，否则会一直等待，从而阻塞主线程造成 ANR 
        
    使用 Preference DataStore 带来的优点：
    1). DataStore 是基于 Flow 实现的，所以保证了在主线程的安全性
    2). 自动完成 SharedPreferences 迁移到 DataStore，保证数据一致性，不会造成数据损坏
    3). 可以监听数据操作

   ![Image text](https://3.bp.blogspot.com/-Vk_q5hWw6DQ/X00ZfiRrB9I/AAAAAAAAPlo/u-kvBvmMfzgRnNViYLwaAim-E7wq5yxKACLcBGAsYHQ/s1600/Screen%2BShot%2B2020-08-31%2Bat%2B11.25.43%2BAM.png)

三、性能如何，基于什么实现
    基于 Flow 和 protocol buffers
    使用Kotlin协程以及Flow异步存储数据
    protocol buffers 将对象序列化存储在本地（磁盘）
    Protocol Buffers ：它是 Google 开源的跨语言编码协议，可以应用到 C++ 、C# 、Dart 、Go 、Java 、Python 等等语言，
    Google 内部几乎所有 RPC 都在使用这个协议，使用了二进制编码压缩，体积更小，速度比 JSON 更快，但是缺点是牺牲了可读性。
    

四、如何使用

JetPack DataStore 有两种实现方式：

1.Preferences DataStore：以键值对的形式存储在本地和 SharedPreferences 类似
   Preference DataStore 主要是为了解决 SharedPreferences 所带来的性能问题。

    文件存放目录： data/data/当前包名/files/datastore
Ps: Preferences DataStore 只支持 Int , Long , Boolean , Float , String 键值对数据，适合存储简单、小型的数据。

2.Proto DataStore：存储类的对象（typed objects ），通过 protocol buffers 将对象序列化存储在本地（磁盘）。
    Proto DataStore 通过 protocol buffers 将对象序列化存储在本地，所以首先需要安装 Protobuf 编译 proto 文件
    
    SingleProcessDataStore 单进程实现
    
   在项目中添加 Gradle 插件编译 proto 文件
   plugins {
       id "com.google.protobuf" version "0.8.12"
   }
   
   // 设置 proto 文件位置
   sourceSets {
       main {
           proto {
               // proto 文件默认路径是 src/main/proto
               // 可以通过 srcDir 修改 proto 文件的位置
               srcDir 'src/main/proto'
           }
       }
   }
   
   // 添加依赖
   implementation  "com.google.protobuf:protobuf-javalite:3.11.0"
   

    # 配置 protoc 命令
   protobuf {
       // 设置 protoc 的版本
       protoc {
           // 从仓库下载 protoc 这里的版本号需要与依赖 com.google.protobuf:protobuf-javalite:xxx 版本相同
           artifact = 'com.google.protobuf:protoc:3.11.0'
       }
       generateProtoTasks {
           all().each { task ->
               task.builtins {
                   java {
                       option "lite"
                   }
               }
           }
       }
       // 默认生成目录 $buildDir/generated/source/proto 通过 generatedFilesBaseDir 改变生成位置
       generatedFilesBaseDir = "$projectDir/src/main"
       
   }
   
   
   Preference DataStore 与 Proto DataStore，它们之间有什么区别？
   
   1.Preference DataStore 主要是为了解决 SharedPreferences 所带来的问题,内部也是定义了一个 proto 文件，通过 PreferencesSerializer 将每一对 key-value 数据映射到 proto 文件定义的 message（PreferenceMap） 类型
   
   2.Proto DataStore 比 Preference DataStore  更加灵活，支持更多的类型
   
   3.Preference DataStore 支持 Int 、 Long 、 Boolean 、 Float 、 String
     protocol buffers 支持的类型，Proto DataStore 都支持
   
   4.Preference DataStore 以 XML 的形式存储 key-value 数据，可读性很好
   
   5.Proto DataStore 使用了二进制编码压缩，体积更小，速度比 XML 更快
   
   接下来开始创建一个 proto 文件：
   
       // 指定 protobuf 的版本，如果没有指定默认使用 proto2，必须是.proto文件的除空行和注释内容之外的第一行
       // proto3 学习的官网 https://developers.google.com/protocol-buffers/docs/proto3
       syntax = "proto3";
       
       // option ：表示一个可选字段
       // java_package ： 指定生成 java 类所在的包名
       option java_package = "com.kuanquan.datastore.proto";
       
       // java_outer_classname ： 指定生成 java 类的名字
       option java_outer_classname = "UserProtos";
       
       // java_multiple_files 该选项为true时，生成的Java类将是包级别的，否则会在一个包装类中
       option java_multiple_files = true;
       
       // message 中包含了一个 string 类型的字段(name)。注意 ：= 号后面都跟着一个字段编号(类似于数据库的主键，用于在编译后的二进制消息格式中对字段进行识别)
       message UserPreferences {
       
           // 格式：字段类型 + 字段名称 + 字段编号
           string name = 1;
           
           // 每个字段由三部分组成：字段类型 + 字段名称 + 字段编号，在 Java 中每个字段会被编译成 Java 对象
           int32 age = 2;
           
           // repeated 修饰的等价于 java 中的 List<VipUser> vips
           repeated VipUser vips = 3;
       }
       
       message VipUser {
           string avatar = 1;
           string vipTag = 2;
           string name = 3;
           int32 age = 4;
       }
   
   下面是罗列常用的映射
   proto3      Java
   string  ->  String
   int32  ->   int
   int64  ->   long
   bool  ->    boolean
   float  ->   float
   double  ->  double
   
  开始构建 DataStore:
  
  实现了 Serializer<User>  接口，这是为了告诉 DataStore 如何从 proto 文件中读写数据
  User 是通过编译 proto 文件生成的 Java 类
  User.parseFrom(input) 是编译器自动生成的，用于读取并解析 input 的消息
  t.writeTo(output) 是编译器自动生成的，用于写入序列化消息
  
    object UserSerializer: Serializer<User> {
            override fun readFrom(input: InputStream): User {
                try {
                    return User.parseFrom(input) // 是编译器自动生成的，用于读取并解析 input 的消息
                } catch (exception: InvalidProtocolBufferException) {
                    throw CorruptionException("Cannot read proto.", exception)
                }
            }
    
            override fun writeTo(t: User, output: OutputStream) {
                t.writeTo(output) // t.writeTo(output) 是编译器自动生成的，用于写入序列化消息
            }
    
    }
    
    private val dataStore: DataStore<User> =
            createDataStore(
                fileName = "user_prefs.pb", // filename参数告知数据存储到用于存储数据的文件
                serializer = UserSerializer
            )
            
  读取数据：
  DataStore 是基于 Flow 实现的，所以通过 dataStore.data 会返回一个 Flow<T>，每当数据变化的时候都会重新发出
   
    private fun readData(): Flow<User> = dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(User.getDefaultInstance())
                } else {
                    throw it
                }

    readData().asLiveData().observe(this, Observer {
                    Log.e(TAG, "asLiveData -> $it")
                })
                
  写入数据：
  在 Proto DataStore 中是通过 DataStore.updateData() 方法写入数据的，DataStore.updateData() 是一个 suspend 函数，
  所以只能在协程体内使用，每当遇到 suspend 函数以挂起的方式运行，并不会阻塞主线程。
  user.toBuilder() 是编译器为每个类生成 Builder 类，用于创建消息实例。
  
  
      private suspend fun saveData(data: User) {
              dataStore.updateData { user ->
                  user.toBuilder().setAge(data.age).setName(data.name).build()
              }
          }
   