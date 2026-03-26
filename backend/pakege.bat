#!/bin/bash
# package-dist.sh - 收集所有 JAR 并打包成 zip

echo "========================================"
echo "  创建分发包"
echo "========================================"

# 1. 先执行 Maven 打包
echo "[1/4] Maven 打包..."
mvn clean package -DskipTests

# 2. 创建临时目录
echo "[2/4] 创建分发目录..."
rm -rf dist
mkdir -p dist/services
mkdir -p dist/config

# 3. 复制需要的 JAR（只复制可执行服务）
echo "[3/4] 复制 JAR 文件..."
cp eureka-server/target/eureka-server.jar dist/services/
cp gateway-server/target/gateway-server.jar dist/services/
cp system-service/system-provider/target/system-provider.jar dist/services/

# 4. 复制 JRE（如果存在）
if [ -d "jre" ]; then
    echo "复制 JRE..."
    cp -r jre dist/
fi

# 5. 复制配置文件
if [ -d "config" ]; then
    echo "复制配置文件..."
    cp -r config/* dist/config/
fi
