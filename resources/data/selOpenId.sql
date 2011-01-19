--Frontierドメインとmidからユーザ情報を検索
--openid認証の際に利用する
select
    b.openid,
    b.mid,
    b.nickname,
    b.entdate,
    b.membertype
from
	members as b,
	frontier_user_management c
where
    c.frontierdomain = /*frontierdomain*/ and
    c.fid = /*fid*/ and
	b.status = '1' and
	b.mid = c.mid