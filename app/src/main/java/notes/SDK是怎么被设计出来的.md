## 数据SDK
    定位为抽象程度较高的基础SDK，没有复杂的业务场景，专注于提供好用的Api
    ,提升开发者的编码(coding)体验;
## 怎么实现好呢
    如果现在让你从0设计并实现一个数据服务SDK，你有什么思路吗，或者该从哪下手呢：
    1 类似产品的借鉴？ parse？
    2 其他领域的思路借鉴？ ORM in JavaWeb
   
## put方式怎么样
    例子：parse
    缺点：
        1 不能有效依赖于IDE的静态类型检查，出现低级错漏更容易；
        2 在子类化问题上比较尴尬
        
## 另辟蹊径 -- 参照ORM库
     不妨把所有数据抽象为BmobObject，基类中写好了基础的save，update，remove等方法，需要扩展字段的话，就写一个子类并在子类中添加吧；
     class BmobObject{
        // ...
        
        public void save(){
            // ...
        }
        
        public void update(){
            // ...
        }
        
        public void remove(){
            // ...
        }
     }
## 回调接口的设计
    倒推 -- 你希望开发者用起来是怎样的？
    class Person extends BmobObject{
        // ...
    }
    
    Person nothing = new Person();
    nothing.setXxx("");
    nothing.save(new SaveListener<String>(){
        public void onSuccess(String objectId){
            // ... 
        }
        
        public void onFailure(BmobException){
            // ... 
        }
    })
    
    接口的复用与合并
    
    细节 SDK回调方法在哪个线程被执行
## 查询的套路
    因为查询需求较多，也经常会有复杂查询的需求，所以单独提取出来一个BmobQuery类：
    class BmobQuery{
        
        // 表名
        // 设置跳过
        // 设置查询条数
        // 设置排序
        // ...
    }
    查询条件的封装
    class QueryCondiction{
        // 各种设置查询条件
    }
    
## 用户系统 
    BmobUser
    扩展 -- 子类化
    登录
        用户名 + 密码 
        邮箱
        手机号
    注册
        用户名 + 密码 
        邮箱
        手机号
    重置密码(密码找回)
    对用户表的修改
        sessionTokrn
    查询用户状态
    缓存用户CurrentUser
    fetchUserInfo
        获取后端用户表的最新数据
## 批量操作
    背景：节省请求Api数
    在BmobObject中提供
    单独提取出来提个BmobBatch类
## 缓存相关
    查询数据相关的缓存策略
    
## 对sql的模拟和支持 -- bql
    模拟出的能直接传sql语句，暂时支持查询相关
## 实时数据监听
    客户端能实时监听到来自后端数据的改变
## 数据关联的建模
    数据关联
        一对多
        多对多
    Pointer 和 Relation
## 错误码的设计
    客户端(SDK)定义的错误码
    restful返回的错误码
    
## 反思既有的设计
    1 Api层
    2 数据关联部分
## SDK的层次
    1 curd层
    2 api层
    3 网络层(加解密)
## 对可扩展和可维护负责  
## 慎用注解
    这里指的是 慎重提供SDK内的自定义注解
    注解容易带来不紧凑的代码，如果提供，建议在SDK中做检查开发者是否忘了加特定注解；
## 改善体验
    这里的体验，其实可以说从开发者下载集成SDK，到开始编码完成第一个请求，再到开发者的调试，上线后App更新的完整链路；
    1 SDK集成成本与效率
    2 Api的友好程度
    3 完善细致的文档
    4 SDKDemo和最佳实践
        SDKDemo的重要性
        (1) 能run起来的代码示例
        (2) 统一测试环境
            很多开发者会去工单返回某功能不行，此时可让其对照测试SDKDemo中的对应功能；
        (3) 对照配置
            开发者遇到bug时，如果SDKDemo测试没问题，那就检查下App项目和SDKDemo的配置(三方依赖，权限声明等)
        
    5 视频教程
    6 工单和社区，技术支持
    7 升级(替换)SDK的成本
    8 版本号
    
## 建议提交工单的合理方式
    技术支持经验谈：
    1 举例
        怎么把查询到的数据显示到listview上呢
        怎么加载查询到的图片呢 最好能发代码给我
        登录不能用了 
        报错了 有bug了 
        显示加载活动失败 怎么办 是不是有bug了 
        
    建议方式：
        建议提供以下信息：
        1 基础功能不能用的话，可以对照SDKDemo测试下；
        2 是必现还是偶发，是同样的代码有时会请求会报错？ 
        3 最好提供下复现的方法，以供这边的工程师测试
        4 工单免费 但请不要重复提交 
        5 SDK版本号请合适勾选
    