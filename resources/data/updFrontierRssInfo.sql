--RSS設定情報の更新
update 
    frontier_rss_info 
set 
    patternname = /*patternname*/,
    filename = /*filename*/,
    detail = /*detail*/,
    updid = /*updid*/,
    upddate = now()
where
    id = /*id*/