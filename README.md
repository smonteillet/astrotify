Astrotify is a project that notifies you when nice things occurs from an astronomical point of view :
- when evening will be worth getting out your telescope
- daily celestial body ephemeride

# Compile

To compile and run test, use maven `mvn clean package`

## Architecture

Astrotify use [Meteoblue](https://content.meteoblue.com/en/spatial-dimensions/air/astronomy-seeing) to gather astronomical weather data near your location.

Astrotify use [SkyLive](https://theskylive.com/) to gather celestial body data.

Astrotify use Google Cloud Platform and is deployed as a Cloud Function. It is triggered by topic subscription, sent by a Cloud Scheduler on a daily basis.

Currently, Astrotify will notify you on Telegram using [Astrotify Telegram Bot](http://t.me/AstrotifyBot)

# Deploy

To deploy astrotify to GCP, use `gcp-function-deploy.sh`
Astrotify also use Google Secret Manager to store/fetch Astrotify Telegram Bot API Token. Secret ID is `ASTROTIFY_TELEGRAM_BOT_TOKEN`.
Astrotify will need specific env vars depending on the use case.

## Check weather use case

PubSub Message will need the following env vars : 
 - `FUNCTION` : CHECK_WEATHER
 - `METEOBLUE_URL` : Meteoblue URL for your location
 - `TELEGRAM_CHAT_ID` : Telegram chat id where you will be notify (look at this [tutorial](https://rieckpil.de/howto-send-telegram-bot-notifications-with-java/) in order to get chat Id)

## Celestial Body ephemeride use case

PubSub Message will need the following env vars :
- `FUNCTION` : CELESTIAL_BODY
- `METEOBLUE_URL` : Meteoblue URL for your location, used for weather
- `TELEGRAM_CHAT_ID` : Telegram chat id where you will be notify (look at this [tutorial](https://rieckpil.de/howto-send-telegram-bot-notifications-with-java/) in order to get chat Id)
- `SKY_LIVE_URL` : Sky live URL for your location, used for celestial body data
- `CITY` : The city where you want to observe the celestial body name
- `CELESTIAL_BODY` : The celestial body name

