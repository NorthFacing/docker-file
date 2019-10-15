# docker-file
本项目用于搭建rascms跨平台编译环境。

# 使用仓库镜像

因为之前已经构建过镜像，并推送到了镜像仓库，所以现在只需要拉取镜像即可：

```bash
$ docker search rascms
      NAME                DESCRIPTION         STARS               OFFICIAL            AUTOMATED
      adolphor/rascms                         0
$ dockertags adolphor/rascms
      latest
$ docker pull adolphor/rascms
      Using default tag: latest
      latest: Pulling from adolphor/rascms
      Digest: sha256:307badaf4f05bab536f4433ede0317229d83d84f44f983620afccdd046c09084
      Status: Image is up to date for adolphor/rascms:latest
      docker.io/adolphor/rascms:latest
```


# 第一次构建

创建编译环境的时候不需要git clone本项目到本地，只需要 `Dockerfile` 文件即可，所有的SDK下载都在构建镜像的过程中自动拉取。

## 构建
```bash
docker build -t adolphor/rascms .
```

## 运行

### 第一次初始化
```bash
docker run --name rascms -i -v /Users/adolphor/IdeaProjects/joyoung:/root/joyoung/workspace -t adolphor/rascms /bin/bash
```

### 启动已经存在容器
```bash
docker start rascms -i
```

### 进入已经启动的容器

```bash
docker exec -it 6e /bin/bash
```
