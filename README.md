# LMRecyclerView
支持加载更多的RecyclerView

###功能
* 支持加载更多
* 支持自定义加载布局
* 支持线性，网格，瀑布流

### 注意事项
* 使用网格布局`GridLayoutManager`时,要保证首次注入的数据源的数目是每行要显示的数据的整数倍，示例代码如下：

```
@Overridepublic void onActivityCreated(@Nullable Bundle savedInstanceState) {
super.onActivityCreated(savedInstanceState);  
initDatas(); 
//此处设置支持每行最多显示两个，那么数据源mData.size 就要是2的
//    整数倍（这里只针对首次初始化，后续就可以任意添加数据了）
mLMRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
mAdapter = new GridAdapter(getActivity(), mDatas); 
mLMRecyclerView.setAdapter(mAdapter); 
mLMRecyclerView.setOnRecyclerLoadMoreListener(this);
}

private void initDatas(){ 
mDatas.clear();  
for(int i=0;i<images.length;i++){
mDatas.add(images[i]);  
mDatas.add(images[i]); 
}}

```
####效果图

![iconfont-screen.gif](http://upload-images.jianshu.io/upload_images/1928103-8cafb61a52c74abc.gif?imageMogr2/auto-orient/strip)

####如何使用
* 使用方式和使用RecyclerView一样
* 实现`OnRecyclerLoadMoreListener`接口进行监听数据加载
* 最后调用 `loadComplete(boolean isSuccess, boolean isHasMore)`,通知`LMRecyclerView`加载完成。

####自定义加载布局请参考demo
*  [DefaultLoadMoreView](https://github.com/cocolove2/LMRecyclerView/blob/master/app/src/main/java/com/cocolove2/lmrecyclerviewdemo/lmRecyclerview/DefaultLoadMoreView.java)
