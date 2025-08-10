public class ShortLinkTableShardingTest {
    public static final String SQL = "CREATE TABLE `t_link_stats_today_%d` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `gid` varchar(32) DEFAULT 'default' COMMENT 'Group Identifier',\n" +
            "  `full_short_url` varchar(128) DEFAULT NULL COMMENT 'Short URL',\n" +
            "  `date` date DEFAULT NULL COMMENT 'Date',\n" +
            "  `today_pv` int(11) DEFAULT '0' COMMENT 'Today Page Views',\n" +
            "  `today_uv` int(11) DEFAULT '0' COMMENT 'Today Unique Visitors',\n" +
            "  `today_ip_count` int(11) DEFAULT '0' COMMENT 'Today IP Count',\n" +
            "  `create_time` datetime DEFAULT NULL COMMENT 'Creation Time',\n" +
            "  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',\n" +
            "  `del_flag` tinyint(1) DEFAULT NULL COMMENT 'Deletion Flag 0: Not Deleted, 1: Deleted',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `idx_unique_today_stats` (`full_short_url`,`gid`,`date`) USING BTREE\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;\n" ;

    public static final String SQL_T_GROUP = "create table t_group_%d\n" +
            "(\n" +
            "    id          bigint auto_increment comment 'id'\n" +
            "        primary key,\n" +
            "    gid         varchar(32)  null comment 'group id',\n" +
            "    name        varchar(64)  null comment 'group name',\n" +
            "    username    varchar(256) null comment 'group username',\n" +
            "    create_time datetime     null comment 'creat timestamp',\n" +
            "    update_time datetime     null comment 'update timestamp',\n" +
            "    del_flag    tinyint(1)   null comment 'delete flag; 0: not delete, 1: deleted',\n" +
            "    sort_order  int          null comment 'sort order',\n" +
            "    unique key `idx_unique_username_gid` (`gid`, `username`) using btree\n" +
            ")char set =utf8mb4;";

    public static final String SQL_T_LINK = "create table t_link_%d\n" +
            "(\n" +
            "    id              bigint auto_increment comment 'ID'\n" +
            "        primary key,\n" +
            "    domain          varchar(128)                   null comment 'domain name',\n" +
            "    short_uri       varchar(8) collate utf8mb3_bin null comment 'short link uri address',\n" +
            "    full_short_url  varchar(128)                   null comment 'full short link url address',\n" +
            "    origin_url      varchar(1024)                  null comment 'original link url address',\n" +
            "    click_num       int default 0                  null comment 'click number',\n" +
            "    gid             varchar(32)                    null comment 'group ID',\n" +
            "    favicon         varchar(256)                   null comment 'website icon'," +
            "\n" +
            "    enable_status   tinyint(1)                     null comment 'Enable Flag; 0: disabled, 1: enabled',\n" +
            "    created_type    tinyint(1)                     null comment 'Create Type; 0: Console, 1: Interface',\n" +
            "    valid_date_type tinyint(1)                     null comment 'Validate Type; 0: permanent, 1: customized',\n" +
            "    valid_date      datetime                       null comment 'validate date range',\n" +
            "    `describe`      varchar(1024)                  null comment 'desc',\n" +
            "    create_time     datetime                       null comment 'create time',\n" +
            "    update_time     datetime                       null comment 'update time',\n" +
            "    del_flag        tinyint(1)                     null comment 'Delete Flag; 0: not delete, 1: deleted',\n" +
            "    constraint idx_unique_full_short_uri\n" +
            "        unique (full_short_url)\n" +
            ");\n";

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf((SQL) + "%n", i);
        }
    }
}
