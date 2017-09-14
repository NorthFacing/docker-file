# docker-file
本项目用于搭建rascms跨平台编译环境

# 构建
```
docker build -t adolphor/rascms .
```

# 运行

## 第一次初始化
```
docker run --name rascms -i -v /Users/adolphor/IdeaProjects/joyoung:/root/joyoung/workspace -t adolphor/rascms /bin/bash
```

## 启动已经存在容器
```
docker start rascms -i
```
