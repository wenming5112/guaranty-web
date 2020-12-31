#!/bin/bash
######################################################################################
###### Operation System: CentOS 7.5
###### desc: Init service Running environment
###### date: 2019/04/14 15:27
###### author: ming
######################################################################################


######################################################################################
###### Install Fabric Network Environment
######################################################################################

# Install tools
installTools(){
    echo
    echo "##########################################################"
    echo "#####                 Install tools                  #####"
    echo "##########################################################"

    yum -y install openssl-devel \
    openssl \
    gcc-c++ \
    make \
    wget \
    curl \
    tree
}


# Shutdown firewalld or setup the rules (test status: ok)
setupFirewalld(){
    echo
    echo "##########################################################"
    echo "#####               Setup firewalld                  #####"
    echo "##########################################################"

    which firewall-cmd >& /dev/null
    NO_FIREWALL=$?
    if [[ "${NO_FIREWALL}" == 0 ]]; then
        systemctl disable firewalld
        systemctl stop firewalld
        systemctl status firewalld
    else
        echo "该主机未安装防火墙"
#        # 安装防火墙程序
#        yum install -y firewalld
#		# 防火墙开机启动
#		systemctl enable firewalld
#		# 重启防火墙
#		systemctl restart firewalld
#		# 查看防火墙状态
#		systemctl status firewalld
    fi

#    # 开放端口(PS: 记得在云服务控制台开放端口)
#    firewall-cmd --zone=public --permanent \
#    --add-port=80/tcp --add-port=3306/tcp \
#    --add-port=6379/tcp --add-port=22/tcp
#
#    # 重新载入
#    firewall-cmd --reload
#    # 查看所有开放的端口
#    firewall-cmd --zone=public --list-ports
}


# Shutdown SeLinux (test status: ok)
setupSeLinux(){
    echo
    echo "##########################################################"
    echo "#####               Setup SeLinux                    #####"
    echo "##########################################################"

    status=`grep SELINUX= /etc/selinux/config | tail -1 | awk -F '='  '{print $2}'`

    if [[ ${status} != disabled ]] ;then
        sed -i 's#SELINUX=enforcing#SELINUX=disabled#g' /etc/selinux/config
        status=`grep SELINUX= /etc/selinux/config | tail -1 | awk -F '='  '{print $2}'`
        if [[ ${status} == disabled ]] ;then
            echo "***********—Modify SeLinux config success-***********"
        else
            echo "***********—Modify SeLinux config Failed!!!-***********"
        fi
    else
        echo "***********—SeLinux config already Modified-***********"
    fi

    setenforce 0
}


# Setup server time synchronization from ntp server (test status: ok)
setupTimeSync(){
    echo
    echo "##########################################################"
    echo "#####               Setup time sync                  #####"
    echo "##########################################################"

    if [[ -d "/etc/timezone/" ]]; then
        echo "rm -rf /etc/timezone/*"
        rm -rf /etc/timezone/
        mkdir -p /etc/timezone/
        echo 'Asia/Shanghai' > /etc/timezone/timezone
    else
        mkdir -p /etc/timezone/
        echo 'Asia/Shanghai' > /etc/timezone/timezone
    fi

    yum install -y ntp ntpdate
    systemctl start ntpd
    systemctl enable ntpd
    # 同步网络时间
    ntpdate cn.pool.ntp.org
    # 将系统时间写入硬件时间
    hwclock --systohc
    # 强制系统时间写入CMOS中防止重启失效
    hwclock -w

    timedatectl set-timezone Asia/Shanghai
    timedatectl set-ntp yes

    rm -rf /etc/localtime
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
}


# Setup server language env (test status: ok)
setupLanguage(){
    echo
    echo "##########################################################"
    echo "#####              Setup Language env               #####"
    echo "##########################################################"

    # 安装中文包
    yum install -y kde-l10n-Chinese

    # echo 'LANG=en_US.UTF-8'  > /etc/sysconfig/i18n
    echo 'LANG=zh_CN.UTF-8'  > /etc/sysconfig/i18n
    source /etc/sysconfig/i18n
    echo
    locale
}


# Update yum repo (test status: ok)
setupYumSource(){
    echo
    echo "##########################################################"
    echo "#####                Setup yum repo                  #####"
    echo "##########################################################"
    which wget >& /dev/null
    NO_WGET=$?
    if [[ "${NO_WGET}" != 0 ]]; then
        yum install -y wget
    fi
    cd /etc/yum.repos.d/
    mv CentOS-Base.repo CentOS-Base.repo.bak
    wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
    echo
    echo "#####      Yum clean      #####"
    yum -y clean all
    echo "#####      To generate cache      #####"
    yum makecache
    echo
}


# Whether to install docker env
installDocker(){
    which docker >& /dev/null
    NO_DOCKER=$?
    if [[ "${NO_DOCKER}" == 0 ]]; then
        echo
        echo "##########################################################"
        docker -v
        echo "##########################################################"
    else
        echo
        echo "##########################################################"
        echo "#####                Install docker                  #####"
        echo "##########################################################"
        yum install -y yum-utils device-mapper-persistent-data lvm2
        # yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
        yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
        yum-config-manager --enable docker-ce-edge
        yum-config-manager --enable docker-ce-test
        yum install -y docker-ce
        systemctl start docker
        docker --version
        chkconfig docker on
    fi
}


# Speed up docker
# 可以在https://www.daocloud.io/网站有加速地址。
setupDockerImageSource(){
    echo
    echo "##########################################################"
    echo "#####                Speed up docker                 #####"
    echo "##########################################################"
    mkdir -p /etc/docker
    # 阿里云加速：https://j1i67hhm.mirror.aliyuncs.com
    # 打开这个地址：http://cr.console.aliyun.com/cn-hangzhou/instances/mirrors
    # 使用支付宝快捷登录阿里云可以获取镜像地址
    # Docker版本要求≥1.12
    curl -sSL https://get.daocloud.io/daotools/set_mirror.sh | sh -s https://j1i67hhm.mirror.aliyuncs.com
    systemctl daemon-reload
    systemctl restart docker
}


# Install docker-compose env
installDockerCompose(){
    which docker-compose >& /dev/null
    NO_DOCKER_COMPOSE=$?
    if [[ "${NO_DOCKER_COMPOSE}" == 0 ]]; then
        echo
        echo "##########################################################"
        docker-compose -version
        echo "##########################################################"
    else
        echo
        echo "##########################################################"
        echo "#####              Install docker-compose            #####"
        echo "##########################################################"

        # 建议将此文件下载下来，访问国外网速度很慢
        # curl -L https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
        # 使用国内镜像
        curl -L https://get.daocloud.io/docker/compose/releases/download/1.24.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
        # mv -f ./docker-compose /usr/local/bin
        chmod +x /usr/local/bin/docker-compose
        docker-compose -version
    fi
}


# 主函数
main(){
    chmod +x ./*.sh
    dos2unix ./*.sh
    # 安装工具
    installTools
    # 设置yum源
    setupYumSource
    # 设置防火墙
    setupFirewalld
    # 设置SeLinux
    setupSeLinux
    # 设置时间同步
    setupTimeSync
    # 设置语言环境
    setupLanguage
    # 安装docker
    installDocker
    # 安装docker-compose
    installDockerCompose
    # 设置docker镜像源
    setupDockerImageSource

    # 用于结局
    echo 1 > /proc/sys/net/ipv4/ip_forward
    echo "net.ipv4.ip_forward = 1" > /etc/sysctl.conf
    sysctl -p /etc/sysctl.conf
}

main
