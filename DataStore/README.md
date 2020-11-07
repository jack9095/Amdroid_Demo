
初窥DataStore

DataStore 是 JetPack 家族的一个库，它提供了一个新的数据存储解决方案，目前处于alpha阶段。

JetPack DataStore 有两种实现方式：

1.Preferences DataStore：以键值对的形式存储在本地和 SharedPreferences 类似
   Preference DataStore 主要是为了解决 SharedPreferences 所带来的性能问题。

   SharedPreferences 目前在使用的过程中存在的问题：
   1). 通过 getXXX() 读取数据会造成主线程阻塞（因为是同步的）
   2). 不能保证类型安全,因为使用相同的 key 进行操作的时候, put 方法可以使用不同类型的数据覆盖掉相同的 key。
   3). apply() 方法是异步的，可能会发生 ANR
Ps: apply() 方法是异步的，本身是不会有任何问题，但是当生命周期处于  handleStopService() 、 handlePauseActivity() 、
    handleStopActivity()  的时候会一直等待 apply() 方法将数据保存成功，否则会一直等待，从而阻塞主线程造成 ANR 


使用 Preference DataStore 带来的优点：
1). DataStore 是基于 Flow 实现的，所以保证了在主线程的安全性
2). 自动完成 SharedPreferences 迁移到 DataStore，保证数据一致性，不会造成数据损坏
3). 可以监听到操作成功或者失败结果

Ps: Preferences DataStore 只支持 Int , Long , Boolean , Float , String 键值对数据，适合存储简单、小型的数据。

2.Proto DataStore：存储类的对象（typed objects ），通过 protocol buffers 将对象序列化存储在本地。
    Proto DataStore 通过 protocol buffers 将对象序列化存储在本地，所以首先需要安装 Protobuf 编译 proto 文件