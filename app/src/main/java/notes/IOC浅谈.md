## IOC浅谈

### 什么是IOC
    Inversion of Control 俗称控制反转
    把代码中需要的对象(组件)交由IOC容器创建和控制
    
    谁控制谁 控制了什么
        在IOC之前都是在应用程序中直接new所需的对象，IOC则用IOC容器负责对象的创建，是IOC容器控制了所需对象(也不局限于对象)
    反转的是什么
        应用开发程序相比IOC容器在对象的创建上被反转了，从主动创建对象到只能被动等待IOC容器(类似工厂)返回的对象；
    
    很多使用侵入性较强的框架的读者应该有所体会，很多时候你连new对象的几乎都没有，这些事情都是IOC框架帮我们做的；
### 正向与反向  
    
### 为何需要IOC
    软件开发的发展慢慢变得模块化，层次化，慢慢有了应用开发者和库/框架开发者的角色分工，应用程序依赖于底层框架和类库才能一起运行。
    没有IOC之前
        组件创建的权利和执行流程都在应用程序开发者；
    有了IOC之后
        IOC容器强势主导了对象的创建和控制，甚至决定程序运行流程；
    
### 典型示例
    理解思路： 对象怎么来(创建)的我不管，我只想直接用，而java中的注解就是连接本地字段和IOC容器的桥梁；
#### in JavaWeb
    在初学JavaWeb几大框架时，第一个概念基本都是IOC+AOP，其实简单理解：
    代码中所需的Dao对象和Service，都不需要你去创建，依赖于注解标记，让framework去创建即可，如下：
    // 只管声明 无需创建
        public class PersonServiceBean implements PersonService {
            @Autowired private PersonDao personDao;
        
            public void setPersonDao(PersonDao personDao) {
                this.personDao = personDao;
            }
        
            @Override
            public void save() {
                // 用到时直接用
                personDao.add();
            }
        }
    
#### in Android
    在Android中也有类似的需求，类似的有ButterKnife，Dagger，AndroidAnnotation等，实现的方式不一，或是运行时的反射，或是编译时生成的代码。
    具体可以看下"写一个简单的注解库"此文。
### 父类(Framework)和子类(Application)
    很多IOC框架都需要开发者写子类，并决定了子类(组件)的创建和生命周期，不管是写JavaWeb应用还是AndroidApp；
    下面结合软件的分工，阐述下Android开发：
    如前文所述，软件的分工越来越倾向于平台提供基础SDK(Framework)，应用开发者基于此写子类组件和业务逻辑；
    1 时间问题
        Framework(比如AndroidSDK)写在前，而App开发者写的子类写在后，SDK中的基类Activity或ActivityThread是不清楚子类名的，因为时间前后问题，故而需要以下开发模式；
    2 开发方式
        开发者写子类(MainActivity）后需要到Manifest.xml(简单理解为子类组件清单，配置文件，类似Spring中的applicationContext.xml)中注册好，并写好对应的类名(MainActivity)和类型(Activity)
        
        <activity android:name=".MainActivity">
             <intent-filter>
                  <action android:name="android.intent.action.MAIN" />
        
                  <category android:name="android.intent.category.LAUNCHER" />
             </intent-filter>
        </activity>
        
        Framework的处理：
            运行时读取XML文件中的组件标签，获取类型(Activity)和类名(MainActivity），并通过反射创建对子类对象；
            读者可以试下写错类型如Service或写错类名，会报ClassCastException和ClassNotFoundException
        为何采用XML
            先说说依赖，如果A类中创建或调用了B类的方法，可以说A依赖了B，没有B类，A类将无法运行，每次修改，也需要单独编译，而采用配置文件的思路，既能单独编译，也能在修改配置文件后不用重新编译或运行，因为都是在运行时读取的，提高了程序的动态性；
            配置文件是和Apk的发布一起打包的，在后端的场景中就比较明显了，修改配置文件，不用重启程序，屡见不鲜；
            
    3 模拟实现
        模拟实现SpingIOC
        
        模拟实现Android组件的创建过程
        
### 和DI的区别
    IOC很容易和DI混淆，DI是依赖注入，简言之：
    DI更侧重于实现方式，把依赖的对象注入进取，在java中，结合java的语法特性，有不同的实现DI的方式；
    IOC偏向于解耦，可以近似理解DI是实现IOC的一种方式。

