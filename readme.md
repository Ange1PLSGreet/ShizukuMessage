# ShizukuMessage

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Customize configuration

See [Vite Configuration Reference](https://vitejs.dev/config/).

## Project Setup

``` gui ``` 界面包 分为desktop和mobile两个包，一个为桌面端，一个为移动端(废弃)

``` image ``` 图形绘制包 存储着绘制SVG图像的实现(废弃) 

```sh
npm run dev
```

### Compile and Minify for Production

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
