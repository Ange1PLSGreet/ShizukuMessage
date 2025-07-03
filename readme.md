## 这是一个加密聊天软件的项目文档，它会对项目的每个部分作出解释

# 请一定要遵守此文档进行开发！！！

# 结构：

## 包名：


``` announce``` 接口包 存储着接口，每个类对应一个接口

``` server ``` 服务器包/客户端包 存储着服务器和客户端的实现 

``` gui ``` 界面包 分为desktop和mobile两个包，一个为桌面端，一个为移动端(废弃)

``` image ``` 图形绘制包 存储着绘制SVG图像的实现(废弃) 

``` cryptor ``` 加密包 存储着加密和解密的实现

## resources xml内容

``` gui_property.xml ```(废弃)

存储着GUI的信息，根据这些信息在``` UnionProperty.java ``` 里编写方法获取

``` log4j2.xml ```

存储着log4j2的配置信息，在生产环境编译时请根据注释屏蔽掉日志输出

## 开发规范：

1. 请不要在代码中使用任何的``` System.out.println() ``` 或者 ```e.printStack()```方法，使用``` log ``` 方法代替

2. 不要使用```throw new Exception()``` 方法，必须使用catch!

3. 当你编写一个类的时候，一定要新建一个接口

4. 尽量避免static的使用

5. 方法和类名一定要使用大驼峰命名法

## 本项目所用的库

1. log4j2

2. junit

3. jupiter

4. flatlaf

5. xmlgraphics