# git 代理
- https://www.cnblogs.com/whm-blog/p/16869052.html
## 1. 直接修改 `.git/config` 文件
`注意: 仅对当前仓库有效, 推荐这种, 因为不是所有的仓库都需要走代理`
```
[http "https://github.com"]
	proxy = http://127.0.0.1:1080
```
或者如下
```
[http "https://github.com"]
	proxy = http://127.0.0.1:1080
[https "https://github.com"]
	proxy = http://127.0.0.1:1080
```

## 2. 命令配置
原文: https://zhuanlan.zhihu.com/p/481574024
### 全局设置（不推荐）

```
#使用http代理

git config --global http.proxy http://127.0.0.1:1080
git config --global https.proxy https://127.0.0.1:1080

#使用socks5代理
git config --global http.proxy socks5://127.0.0.1:1080
git config --global https.proxy socks5://127.0.0.1:1080

```

### 只对Github代理（推荐）
```
#使用socks5代理（推荐）
git config --global http.https://github.com.proxy socks5://127.0.0.1:1080

#使用http代理（不推荐）
git config --global http.https://github.com.proxy http://127.0.0.1:1080
```
### 取消代理
当你不需要使用代理时，可以取消之前设置的代理。
```
git config --global --unset http.proxy git config --global --unset https.proxy

```