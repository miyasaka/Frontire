update oauthTokensStore set
  requestToken = /*requestToken*/'xxx'
 ,accessToken  = /*accessToken*/'xxx'
 ,tokenSecret  = /*tokenSecret*/'xxx'
 ,screenname   = /*screenname*/
 ,twitname     = /*twitname*/
 ,pic          = /*pic*/
where mid        = /*mid*/'m000000001'
  and consumerId = /*consumerId*/'XXX'
  and twituserid = /*twituserid*/
  and delflg    = '0'
