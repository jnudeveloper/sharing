FROM node:14.18.1-buster

#设置NPM使用国内淘宝镜像
RUN npm config set registry https://registry.npm.taobao.org

#复制依赖包配置
COPY ./package.json /tmp/code/package.json

#安装依赖
RUN cd /tmp/code && npm install

#复制代码
COPY ./ /tmp/code/

#打包vue项目
RUN cd /tmp/code && npm run-script build

#重新依赖Nginx
FROM nginx

#设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

#设置变量
ARG DEPENDENCY=/tmp/code

#复制nginx配置文件
COPY --from=0 ${DEPENDENCY}/nginx.conf /etc/nginx/conf.d/default.conf

#复制打包好的前端文件
COPY --from=0 ${DEPENDENCY}/dist /usr/share/nginx/html
