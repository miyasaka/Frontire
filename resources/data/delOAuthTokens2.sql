update oauthTokensStore set
  delflg = '1'
where mid = /*mid*/ and
      twituserid = /*twituserid*/ and
      delflg = '0'