# RouteLineProject
折线图，均线图(日K图)，股市行情折线图，带有手指滑动交互；适用于上证指数，欧元汇率，黄金汇率。

  · 这个均值可以后台计算，也可以前台计算。项目中采用的是后台计算，前台展示(如果我记得还清楚的话)。
  · 因为数据量比较大，哪怕json解析，目前也是子线程解析。

   因为最开始的时候，没有想着把这两个控件开源，所以，从设计模式上来说，就没有更多地去考虑通用。这个工作，可以开发者自己
根据需求，自己去改写。在改写的过程中，如果碰到什么问题，可以给我发邮件，或者issue。我看到的话会回复。


###  折线图（分时图） RouterLineView：

折线图，均线图(日K图)，股市行情折线图，带有手指滑动交互；适用于上证指数，欧元汇率，黄金汇率。

折线图，实时变化图：

·提供了初始化的API，和增加点的方法。

·需要说明的是，一天的变化的点数，不可能全部描绘到手机屏幕的宽度上。所以，先让后台吧所得到的点，按照一天的总点数，平均
到几分钟一个点左右。也就是说，后台 会从数据供应商那里拿来的数据，抛弃很多点的信息。这个是可以理解的。

'''

   RouteLineView routeline = findViewById(R.id.routeline);
   //GOLD, SZHENG, EUROPE,提供三种模式。考虑交易交易时间和像素点数目
   routeline.setType(RouteLineView.TYPE.SZHENG);
//        routeline.setMaxAndMin();
//        routeline.addData();

'''

![折线图](https://github.com/HDHunter/RouteLineProject/blob/master/imgs/ScreenShot00028.png)



### 均线图(日K图) CandleChartView：
同时带有手指交互功能。

![均线图](https://github.com/HDHunter/RouteLineProject/blob/master/imgs/ScreenShot00027.png)


--------

支付宝打赏
![支付宝打赏](https://github.com/HDHunter/RouteLineProject/blob/master/imgs/zhifubao-shouqian.png)

微信打赏
![微信打赏](https://github.com/HDHunter/RouteLineProject/blob/master/imgs/weixin_shouqian.png)

欢迎交流学习。
http://blog.csdn.net/u011216417
email me :zhangjianqiu007@126.com