--RSS設定情報の登録
insert into frontier_rss_info 
(
    id,
    patternname,
    filename,
    detail,
    entid,
    entdate,
    updid,
    upddate
)
values
(
    /*id*/,
    /*patternname*/,
    /*filename*/,
    /*detail*/,
    /*entid*/,
    now(),
    /*updid*/,
    now()
)