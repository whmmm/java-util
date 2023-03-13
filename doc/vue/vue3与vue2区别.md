# vue3 与 vue2 区别
## 动态组件 `<component :is="com">`
vue2 中 `com` 是个组件名(字符串), <br/>
vue3 中 `com` 是个组件变量 (`setup`语法下, 其他没实)


## 生命周期不同

## 自动滚动到底部

```
不知为何
scrollTop = scrollHeight 无效
目前用的是 <a> 链接锚点形式点击跳转到底部
```