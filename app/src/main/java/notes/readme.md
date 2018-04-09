# SDK使用文档
## Pre 快速入门
### SDK集成与导入
   鉴于现在基本是以AndroidStudio为主流的开发环境，以下主要讲在AS中集成和导入SDK的步骤：
   1. 在项目App的build.gradle文件中配置好so文件放置的目录；<br>
   
   ![image](http://bmob-cdn-12948.b0.upaiyun.com/2018/04/09/fd829d514066f03380167174914477a7.png)

   2. 点击同步gradle，会看到左侧多了一个名为jniLibs的文件夹；<br>
   
   ![image](http://bmob-cdn-12948.b0.upaiyun.com/2018/04/09/b3af16d5407a041c80941fb0d2dd909b.png)
   
   3. 将SDK对应的jar包和so文件以及SDK依赖的jar复制到该文件夹下；<br>
   
   4. 此时，你就可以开始在项目中使用CloudCard的相关功能了。
    
### SDK的绑定和初始化
    
## PartA 云卡相关功能
### 1 绑定
   调用CloudCard类的getInstance方法，记得复写ApplicationListener中的getClient回调，SDK需要拿到您的应用ID和终端号，用法如下
  
     CloudCard.getInstance(context, new ApplicationListener() {
        @Override
        public Client getClient() {
            Client client = new Client();
            client.setAppid(Config.APPID);
            client.setTerminal(Config.TERMINAL);
            return client;
        }

        @Override
        public void onSuccess() {
            // ...
        }

        @Override
        public void onWarning(int i) {
            // ...
        }

        @Override
        public void onError(CloudError cloudError) {
            // ...
        }
     });  
        
### 2 初始化
   对于使用cos1.0的开发者，你只需要调用Initialization方法即可，在InitializationCallback中补充对应的UserUnique字段。
    
    MyApplication.getInstance().getCloudCard().Initialization(new InitializationCallback() {
        @Override
        public String getUserUnique() {
            return Config.USERID;
        }
    
        @Override
        public void onSuccess() {
            // ...            
        }
    
        @Override
        public void onError(CloudError cloudError, String s) {
            // ...
        }
    });
    
   对于使用cos2.0的开发者，你还需要调用cosInit方法，具体如下：
   
    MyApplication.getInstance().getCloudCard().cosInit(new InitCosCallback() {
        @Override
        public void onSuccess(String s) {
            log("cos初始化成功");
        }
    
        @Override
        public void onError(CloudError cloudError, String s) {
            log("cos初始化失败" + s);
        }
    
        @Override
        public void onFinished() {
             // ...
        }
    });
   
   其余接口的使用也依赖于初始化接口的成功调用。


### 3 用户开卡
   开通云卡，SDK客户端会从后端下载好卡数据，调用如下：
    
    // 1 构建一个Card对象，传入你的CardCode和CardType
    Card card = new Card();
    card.setCardCode(Config.CARDCODE);
    card.setCardType(Config.CARDTYPE);
    
    SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
    Date curDate = new Date(System.currentTimeMillis());
    String time = formater.format(curDate);
    // 2 订单号，时间 + 6位随机数
    String orederNO = time + Utils.getFixLenthString(6);
    // 3 调用开发方法，如果不是第一次使用可以调用没有orderNo参数的重载方法
    MyApplication.getInstance().getCloudCard().applyCard(card, new CardCallback() {
        @Override
        public void onSuccess(Card card) {
            log("卡片申请成功");
            // 此时可适应做下对回调的Card对象的保存
        }
    
        @Override
        public void onProcess(float v, String s) {
            // ...
        }
    
        @Override
        public void onError(CloudError cloudError, String s) {
            log("卡片申请失败 " + cloudError + "：" + s );
        }
    
        @Override
        public void onFinished() {
            // ...
        }
    },orederNO);
 
### 4 卡片充值
   充值云卡，分别传入卡对象，交易信息，回调callback即可，用法如下：
   
    // 1 设置卡信息，假设这里的cityCardInfo是在上一步开卡操作中的onSuccess方法回调的Card对象赋值过来的
    Card card = new Card();
    card.setId(cityCardInfo.getId());
    card.setCardCode(cityCardInfo.getCardCode());
    card.setCardType(cityCardInfo.getCardType());
    
    // 2 构造交易信息参数
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String data = formatter.format(new Date(System.currentTimeMillis())) + "123456";
    // 这里可能容易出错，请仔细校对
    String transInfo = "{transType:\"0201\",transAmount:\"" + amount + "\"+,orderNo:\"" + data + "\"}";
    // 3 调用充值方法
    MyApplication.getInstance().getCloudCard().rechargeCard(card, transInfo, new CardCallback() {
        @Override
        public void onSuccess(Card card) {
            log("充值成功");
        }
    
        @Override
        public void onProcess(float v, String s) {
            // ...
        }
    
        @Override
        public void onError(CloudError cloudError, String s) {
            log("充值失败" + cloudError + ":" + s);
        }
    
        @Override
        public void onFinished() {
            // ...
        }
    });
    
### 5 查询云卡信息
   查询云卡信息，可以查看诸如逻辑卡号，余额等信息，用法如下：
   
    // 1 设置卡片信息
    Card card = new Card();
    card.setCardType(Config.CARDTYPE);
    card.setCardCode(Config.CARDCODE);
    // 2 查询
    TransportationCard cardInfo = MyApplication.getInstance().getCloudCard().getTransportationCard(card);
    log("卡号：" + cardInfo.getLogicId());
    log("余额：" + cardInfo.getBalance());

### 6 查询交易记录
   查询云卡交易记录，包含诸如刷卡，充值，时间，余额等信息，用法如下：
   
    // 参数分别对应卡信息，分页索引，条数
    String result = MyApplication.getInstance().getCloudCard().getOnLineTransactionList(cityCardInfo, 0, 30);

   注意：该方法不能在主线程中直接调用，建议新开线程调用，参见SDKDemo中的例子代码。

### 7 更新卡
   更新云卡，如果本地有未上传的交易记录，会先上传后下载卡数据到本地，不过你只需要知道简单用法即可：
   
    MyApplication.getInstance().getCloudCard().update(new CardCallback() {
        @Override
        public void onSuccess(Card card) {
            log("更新云卡信息成功");
        }
    
        @Override
        public void onProcess(float v, String s) {
            // ...
        }
    
        @Override
        public void onError(CloudError cloudError, String s) {
            log("更新云卡信息失败," + cloudError + ": " + s);
        }
    
        @Override
        public void onFinished() {
            // ...
        }
    }); 
    
### 8 获取卡状态
   获取云卡状态，true表示正常能使用，用法如下：
   
    MyApplication.getInstance().getCloudCard().getCardState();
### 9 清除卡片
   清除本地的卡数据，在切换用户的时候调用，调用后需要重新初始化才能继续调用其他接口，使用如下：
   
    MyApplication.getInstance().getCloudCard().clean(new CardCallback() {
        @Override
        public void onSuccess(Card card) {
            log("清卡成功");
        }
    
        @Override
        public void onProcess(float v, String s) {
            // ...
        }
    
        @Override
        public void onError(CloudError cloudError, String s) {
            log("清卡失败 " + cloudError + ":" + s);
        }
    
        @Override
        public void onFinished() {
            // ...
        }
    });

注意：不要使用成功回调时的card参数，因为此时回调过来的是null。

## PartB 云码相关功能
   基本使用流程为：调用时设置卡的CardCode信息，请求生码，SDK接收到加密后的二维码字符串后解密，解密后回调给开发者，开发者可以用zxing库解析字符串并加载出二维码。<br>
   以下的例子基本是SDK的几个生码接口的使用，至于怎么用zxing库解析二维码字符串可以参见SDKDemo中的相关例子代码。
### 二维码开户
   开通二维码账户，使用如下：
   
    MyApplication.getInstance().getCloudCard().applyQrCode(Config.USERID, Config.CARDTYPE, Config.CARDCODE, new QrcodeOpenCallback() {
        @Override
        public void onSuccess(RespQrCodeOpenAccount respQrCodeOpenAccount) {
            log("开码成功");
            log(respQrCodeOpenAccount.getAccountNo());
            log(respQrCodeOpenAccount.getUid());
        }

        @Override
        public void onError(CloudError cloudError, String s) {
            log("开户失败");
            log(cloudError.toString() + "::" + s);
        }

        @Override
        public void onFinished() {
            // ...
        }
    });    
     
### 佳都二维码
   区别与交通部下的二维码，具体使用如下：
            
    MyApplication.getInstance().getCloudCard().getQrcodeDate(Config.CARDCODE,Config.TELEPHONE, Config.UID, Config.USERID, new QrcodeDataCallback() {
        @Override
        public void onSuccess(QrCodeData qrCodeData) {
            // 这里你可以拿到一些参数 SDKDemo中是跳转到新的页面去展示二维码
            Intent intent = new Intent();
            intent.putExtra("mode","0");
            intent.putExtra("CertUselessTime",qrCodeData.getCertUselessTime());
            intent.putExtra("uid",qrCodeData.getUid());
            intent.putExtra("uiid",qrCodeData.getUiId());
            intent.putExtra("certStartTime",qrCodeData.getCertStartTime());
            intent.setClass(QrcodeActivity.this,QrCodeShowActivity.class);
            startActivity(intent);
        }
    
        @Override
        public void onError(CloudError cloudError, String s) {
            // ...
        }
    
        @Override
        public void onFinished() {
            // ...
        }
     },false);
    
   关于最后一个参数isOldType，是为了区分是否用旧版本的方式，如果你用的是旧版本的话，可以直接调用没有isOldType参数的重载方法。
   
### 在线二维码
   在线生成交通部的二维码，用法如下：
    
    MyApplication.getInstance().getCloudCard().getOnlineQrcodeStr(Config.USERID, Config.TELEPHONE, "2017120110581325", "0009", new QrcodeCallback() {
        @Override
        public void onSuccess(String s) {
            //结果用handle 回调展示
            Intent intent = new Intent();
            intent.putExtra("mode", "1");
            intent.putExtra("qrCodeStr", s);
            intent.setClass(QrcodeActivity.this, QrCodeShowActivity.class);
            startActivity(intent);
        }
    
        @Override
        public void onError(CloudError cloudError, String s) {
            log("生码失败");
            log(s);
        }
    
        @Override
        public void onFinished() {
            // ...
        }
    });
 
### 离线二维码
   离线生成交通部的二维码，用法如下：
   
    MyApplication.getInstance().getCloudCard().getOfflineQrcodeStr(Config.USERID, Config.TELEPHONE, "2017120110581325", Config.CARDCODE, new QrcodeCallback() {
        @Override
        public void onSuccess(String s) {
            //结果用handle 回调展示
            Intent intent = new Intent();
            intent.putExtra("mode", "2");
            intent.putExtra("qrCodeStr", s);
            intent.setClass(QrcodeActivity.this, QrCodeShowActivity.class);
            startActivity(intent);
        }
    
        @Override
        public void onError(CloudError cloudError, String s) {
            log("生码失败");
            log(s);
        }
    
        @Override
        public void onFinished() {
            // ...
        }
    });
    
### 二维码充值
## PartC 常见错误

