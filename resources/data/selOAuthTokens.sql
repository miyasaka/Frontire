select tokenid,requestToken,accessToken,tokenSecret,twituserid,screenname,twitname,pic,useflg from oauthTokensStore
 where mid        = /*mid*/'m000000003'
   and consumerId = /*consumerId*/'twitter'
   and delflg    = '0'
/*IF twituserid != null*/
   and twituserid = /*twituserid*/
/*END*/
/*IF useflg != null*/
   and useflg = /*useflg*/
/*END*/