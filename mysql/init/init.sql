CREATE USER 'xiaoming'@'%' IDENTIFIED BY 'Mblog112031';
GRANT All privileges ON *.* TO 'xiaoming'@'%';
-- 开放远程访问权限
-- grant all privileges on *.* to 'root' @'%' identified by 'Mblog112031' with grant option;

-- 刷新权限
-- flush privileges;
