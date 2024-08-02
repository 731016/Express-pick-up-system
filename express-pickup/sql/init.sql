create database if not exists expressPickup;
use expressPickup;
# 用户相关
create table if not exists user_info
(
    userId       varchar(255) primary key comment '用户id',
    userName     varchar(255) comment '姓名',
    passWord     varchar(255) comment '密码',
    userRoleId   varchar(255) comment '角色[C,普通用户；B,配送用户；A,管理员]',
    sex varchar(255) comment '性别',
    actualName varchar(255) comment '真实姓名',
    idNumber varchar(255) comment '身份证号',
    schoolId varchar(255) comment '学校代码',
    schoolName varchar(255) comment '学校名称',
    schoolNumber varchar(255) comment '学号',
    phone varchar(255) comment '手机号',
    signInWithType varchar(255) comment '三方登陆类型',
    signInWithId varchar(255) comment '三方登录id',
    freezeTime datetime comment '冻结时间',
    registerTime datetime comment '注册时间',
    disable int comment '是否禁用 【1：启用，0：禁用】'
    ) comment '用户信息';
create table if not exists user_role
(
    id       varchar(255) primary key comment '角色id',
    roleName     varchar(255) comment '角色名称'
    ) comment '用户角色';
insert into user_role (id,roleName) value ('A','管理员');
insert into user_role (id,roleName) value ('B','配送用户');
insert into user_role (id,roleName) value ('C','普通用户');
create table if not exists login_persistence
(
    userId       varchar(255) primary key comment '用户id',
    userName     varchar(255) comment '用户名称',
    salt     varchar(255) comment '密码加密盐值',
    token     varchar(255) comment 'token'
    ) comment '用户登录信息，持久化';

# 订单相关
create table if not exists order_info
(
    orderId  varchar(255) primary key comment '订单id',
    userId     varchar(255) comment '用户id',
    trackCompanyId     varchar(255) comment '快递公司id',
    trackNumber     varchar(255) comment '快递单号',
    pickupName     varchar(255) comment '收货人',
    contactNumber     varchar(255) comment '收货手机号',
    shipAddress     varchar(255) comment '本平台的配送地址 ',
    trackDeliveryAddress     varchar(255) comment '驿站地址（短信中的地址）',
    remark     varchar(255) comment '备注',
    deliveryManId     varchar(255) comment '配送员id',
    shippingReamrk     varchar(255) comment '配送备注,用于记录配送员，修改订单状态时设置',
    orderStatus     int comment '订单状态',
    isDel     int comment '订单是否删除',
    delReason     varchar(255) comment '删除原因',
    createTime     datetime comment '订单创建时间'
    ) comment '用户登录信息，持久化';
create table if not exists payment_info
(
    orderid       varchar(255) primary key comment '订单id',
    paymentMethod     varchar(255) comment '支付方式',
    paymentAmount     double comment '支付金额',
    serialNumber     varchar(255) comment '流水号',
    paymentStatus     int comment '支付状态',
    beneficiaryName     varchar(255) comment '收款方姓名'
    ) comment '支付信息';
create table if not exists order_comment
(
    orderId       varchar(255) primary key comment '订单id',
    userId     varchar(255) comment '订单发布人id',
    userRating     double comment '用户评分',
    comment     varchar(255) comment '用户评价',
    commentTime datetime comment '用户评价时间',
    deliveryManId     varchar(255) comment '配送员id',
    deliveryRating     double comment '配送员评分',
    deliveryComment     varchar(255) comment '配送员评价',
    deliveryTime datetime comment '配送员评价时间'
    ) comment '评价信息';

# 基础信息
create table if not exists province
(
    id       int primary key comment 'id',
    provinceName     varchar(255) comment '省份名称',
    disable     int comment '是否启用'
    ) comment '省份';
insert into province(id,provinceName,disable) value (1,'湖北省',1);
insert into province(id,provinceName,disable) value (2,'湖南省',1);
create table if not exists school
(
    id       int primary key comment 'id',
    schoolName     varchar(255) comment '学校名称',
    provinceId     int comment '省份id',
    level     varchar(255),
    website     varchar(255),
    abbreviation     varchar(255),
    disable     int comment '是否启用'
    ) comment '学校信息';
insert into school(id,schoolName,provinceId,level,website,abbreviation,disable) value (1,'湖北工程学院',1,'','','',1);
insert into school(id,schoolName,provinceId,level,website,abbreviation,disable) value (2,'湖北铁道运输职业学院',1,'','','',1);
create table if not exists track_company
(
    id       varchar(255) primary key comment 'id',
    companyName     varchar(255) comment '公司名称',
    disable     int comment '是否启用'
    ) comment '快递公司';

