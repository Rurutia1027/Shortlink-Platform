public class ShortLinkTableShardingTest {
    public static final String SQL = "create table t_link_%d\n" +
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
