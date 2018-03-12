## 辩证看patterns
## 例子看代码的演变过程

### 对象的创建
    1 new ？
    2 new带来的耦合(依赖)
    3 中介角色 -- 工厂(创建对象的容器)
        factory类似IOC容器
        (1) 简单版本
        (2) factory method
        (3) abstract method
    4 从现有的对象复制 -- clone
        深克隆
        浅克隆
    5 builder
    6 singleton的细节


### 类数量膨胀(滥用继承)的困惑
#### 例子
#### 改善 decorator/bridge
#### 思路 把依赖和具体类型的创建从编译时(静态)延迟到运行时(动态)

### 通知依赖
#### observer
#### 接口回调

### 过多的if-else -- strategy
    思路：方法块 -> 提取为单独的类

### 状态化 

### command

### 控制整体的template method

### 控制相互交互的mediator
    区别于facade
### 最难的visitor

### more