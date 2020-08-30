

### 启用 SFTP 服务

检查 `/etc/ssh/sshd_config` 配置文件，启用下面的配置代码。

```conf
Subsystem sftp	/usr/libexec/openssh/sftp-server
```

### 访问 SFTP 服务

```bash
sftp james@zizhizhan.com

> ls -lah
> cd /opt/bin
> get update-system.sh
```

`sftp james@zizhizhan.com:/opt/bin/yaml2json /tmp`
