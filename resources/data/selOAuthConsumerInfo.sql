select
  consumerId,
  description,
  callbackURL,
  consumerKey,
  consumerSecret,
  requestTokenURL,
  userAuthrizationURL,
  accessTokenURL
from oauthConsumerInfo
where consumerId = /*consumerid*/