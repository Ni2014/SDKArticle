##起因
    前几天一个在某公司的朋友说起来他那边的SDK相对不太规范和好用，使用的人都要在回调方法中用Handler去做线程切换的事情，问我有没好的思路或这边的解决办法。
##经过
    Bmob的数据服务SDK在3.5.0之前网络请求用的是Volley库，使用Volley时，可以发现onSuccess和onFailure方法是在主线程被回调的，也就是SDK内部用Volley的话，就不用自己去做线程切换了，此时就好奇了，带着问题翻Volley的源码。
    
    调用Voleley的newRequestQueue()方法时会调用到RequestQueue的start()方法。
    public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
           File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
   
           String userAgent = "volley/0";
           try {
               String packageName = context.getPackageName();
               PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
               userAgent = packageName + "/" + info.versionCode;
           } catch (NameNotFoundException e) {
           }
   
           if (stack == null) {
               if (Build.VERSION.SDK_INT >= 9) {
                   stack = new HurlStack();
               } else {
                   // Prior to Gingerbread, HttpUrlConnection was unreliable.
                   // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                   stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
               }
           }
   
           Network network = new BasicNetwork(stack);
   
           RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
           queue.start();
   
           return queue;
    }

    进而到NetworkDispatcher的start()方法
    public void start() {
       stop();  
       // Make sure any currently running dispatchers are stopped.
       // Create the cache dispatcher and start it.
       mCacheDispatcher = new CacheDispatcher(mCacheQueue, mNetworkQueue, mCache, mDelivery);
       mCacheDispatcher.start();
    
       // Create network dispatchers (and corresponding threads) up to the pool size.
       for (int i = 0; i < mDispatchers.length; i++) {
            NetworkDispatcher networkDispatcher = new NetworkDispatcher(mNetworkQueue, mNetwork,
                        mCache, mDelivery);
            mDispatchers[i] = networkDispatcher;
            networkDispatcher.start();
       }
    }
    
    NetworkDispatcher是一个Runnable,看下其run()方法
    @Override
    public void run() {
        // ...
        Request<?> request;
        // ... 
        // Perform the network request.
        NetworkResponse networkResponse = mNetwork.performRequest(request);
        // Parse the response here on the worker thread 很明显的注释
        Response<?> response = request.parseNetworkResponse(networkResponse);
        // Post the response back.
        request.markDelivered();
        // 重要字段出现了mDelivery
        mDelivery.postResponse(request, response);
        // ...
    }
    
    // mDelivery是ResponseDelivery类型，ResponseDelivery是interface
    public interface ResponseDelivery {
        /**
         * Parses a response from the network or cache and delivers it.
         */
        public void postResponse(Request<?> request, Response<?> response);
    
        /**
         * Parses a response from the network or cache and delivers it. The provided
         * Runnable will be executed after delivery.
         */
        public void postResponse(Request<?> request, Response<?> response, Runnable runnable);
    
        /**
         * Posts an error for the given request.
         */
        public void postError(Request<?> request, VolleyError error);
    }
    
    // 在创建NetworkDispatcher的时候被初始化
    public NetworkDispatcher(BlockingQueue<Request<?>> queue,
                Network network, Cache cache,
                ResponseDelivery delivery) {
        mQueue = queue;
        mNetwork = network;
        mCache = cache;
        mDelivery = delivery;
    }
    // 在RequestQueue的start()方法中创建了NetworkDispatcher实例
    public void start() {
       stop();  // Make sure any currently running dispatchers are stopped.
       // Create the cache dispatcher and start it.
       mCacheDispatcher = new CacheDispatcher(mCacheQueue, mNetworkQueue, mCache, mDelivery);
       mCacheDispatcher.start();
    
       // Create network dispatchers (and corresponding threads) up to the pool size.
       for (int i = 0; i < mDispatchers.length; i++) {
           NetworkDispatcher networkDispatcher = new NetworkDispatcher(mNetworkQueue, mNetwork,
                mCache, mDelivery);
           mDispatchers[i] = networkDispatcher;
           networkDispatcher.start();
       }
    }
    
    // 在RequestQueue被初始化时初始化mDelivery
    public RequestQueue(Cache cache, Network network, int threadPoolSize,
            ResponseDelivery delivery) {
        mCache = cache;
        mNetwork = network;
        mDispatchers = new NetworkDispatcher[threadPoolSize];
        mDelivery = delivery;
    }   
    // 暮然回首 就在这里了哈哈 ^_^
    public RequestQueue(Cache cache, Network network, int threadPoolSize) {
            this(cache, network, threadPoolSize,
                    new ExecutorDelivery(new Handler(Looper.getMainLooper())));
    }
    // 用主线程的Looper去构造Handler，再去构造ExecutorDelivery，在往上回溯就比较连贯了，开发者使用的上层接口回调方法onSuccess等就是在主线程被调用了。

    v3.5.0开始，SDK内部用rxjava + okhttp3重构，内部用的是rx去做线程切换，集体可以看下 Observable.Transformer，scheduler等Api操作；
  
## 其他SDK的做法
    其实也不一定是要其他的SDK的做法，自己SDK内部拿到主线程的Handler就可以了，具体怎么拿，可以在初始化SDK的时候拿到，因为一般初始化都是在主线程做的，比如推荐在Application的onCreate()方法，
    然后我看了某知名SDK，在方法的层层回调中看到了init方法有对当前线程的判断，若在子线程会报错。
    也看到了其他SDK，有的是用AsyncTask，虽然不太推荐，不过这种做法很容易，因为AsyncTask本身帮你做好了线程的回调。
    值得说一下的是，Okhttp的回调方法是在子线程被调用的。
## 最后
    一个简单的小问题，还是有必要说一下，毕竟SDK的目的就是为了能让开发者方便，易学易用，这个回调细节之前也有一些SDK厂商没注意后面在版本changelog中看到，后续会讲讲较好设计的SDK的思路，毕竟这方面的资料也不多，国内做SDK的除了一些知名的厂就是大厂了。
    另外就是关于看源码了，带着问题看源码效率较高，也需要慢慢锻炼从源码中定位和解决问题的能力！
    
    

   
    
    
    
    
    
    