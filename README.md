# xpllyn
我的个人小站~  
博客和资源将持续更新，敬请关注~  
传送门：http://www.xpllyn.com
---  
个人网站项目http://www.xpllyn.com/chatroom。  
使用spring-boot搭建，集合了博客、书籍下载、留言、github page查询、Netty网页聊天室等功能。  
其中github page查询工具使用了github的api，聊天室使用Netty+Websocket搭建服务，使用Shiro安全框架实现身份验证/登录，采用MySQL作为网站的数据库，使用Redis用于最近聊天记录的缓存，并用其作为辅助实现聊天记录的保存。另外
，每当用户点击打开了好友的聊天窗口，都会向好友发送一条已读回执，用于更新好友那边的已读未读。同时已读回执会保存在Redis中，和聊天记录一起凌晨三点持久化到MySQL中。详细逻辑见博客https://xiepl1997.github.io/。
总的来说实现了群聊、单聊、查询用户、添加好友、聊天记录、聊天消息已读未读等功能。
目前已开发的功能：
* 资源下载
* 个人博客
* 访客留言
* github page博客搜索
    * 利用了Github的开放API实现了GitHubPage博客搜索工具，给使用者提供了一种，能够找到并访问那些没有被常用搜索引擎收录的优质个人博客和站点的方式。
* 聊天室（Netty + WebSocket + Redis）
    * Shiro登录认证
    * 群聊
    * 私聊
    * 加好友
    * 账号互踢机制
    * 聊天记录
    * 聊天消息已读未读功能

正在开发的功能：
* 资源共享

主页截图：  
![image](https://github.com/xiepl1997/xpllyn/blob/master/screenshot/xpllyn1.png)

github page搜索截图：  
![image](https://github.com/xiepl1997/xpllyn/blob/master/screenshot/githubpage.jpg)

聊天室截图：
![image](https://github.com/xiepl1997/xpllyn/blob/master/screenshot/newChatRoom.PNG)